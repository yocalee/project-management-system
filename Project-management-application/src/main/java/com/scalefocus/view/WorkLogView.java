package com.scalefocus.view;

import com.scalefocus.model.*;
import com.scalefocus.repository.*;
import com.scalefocus.service.*;
import com.scalefocus.util.*;
import java.time.LocalDateTime;
import java.util.List;

public class WorkLogView extends View {
    private final TaskService taskService;
    private final Validation.TaskValidation taskValidation;
    private final WorkLogService workLogService;

    public WorkLogView() {
        this.taskService = new TaskService(new TaskRepository());
        this.taskValidation = new Validation.TaskValidation(taskService);
        this.workLogService = new WorkLogService(new WorkingLogRepository());
    }

    @Override
    public void menu(boolean isAdmin) {
        Printer.workLogMenu();
        String option = sc.nextLine().toUpperCase();
        switch (option) {
            case "C": {
                int taskId = findTask(isAdmin);
                if (taskId == -1) {
                    return;
                }
                System.out.println("Describe what you did on this task...");
                String description = sc.nextLine();
                String time = createTime();
                WorkingLog workLog = workLogService.save("time, description, user_id, task_id".split(", "),
                        new Object[]{time, description, UserService.user.getId(), taskId});
                System.out.println("Successfully created work log with id " + workLog.getId());
                break;
            }
            case "E": {
                int taskId = findTask(isAdmin);
                if (taskId == -1) {
                    return;
                }
                List<WorkingLog> logs = workLogService.findInCommonTable("working_logs", " task_id = " + taskId, "id");
                System.out.println("Working logs for current tasks...");
                logs.forEach(l -> System.out.println(l.toString()));
                System.out.println();
                Printer.workLogEditMenu();
                System.out.println("Enter work log id...");
                int worklogId = Integer.parseInt(sc.nextLine());
                WorkingLog worklog = workLogService.findById(worklogId);

                if(worklog == null){
                    System.out.println("Invalid id.");
                    return;
                }

                String[] columns = new String[1];
                Object[] values = new Object[1];
                String opt = sc.nextLine().toUpperCase();
                switch (opt){
                    case "T":
                        columns[0] = "time";
                        values[0] = createTime();
                        break;
                    case "D":
                        columns[0] = "description";
                        System.out.println("Enter new description...");
                        values[0] = sc.nextLine();
                        break;
                }
                workLogService.update(columns, values, " id = " + worklogId);
                System.out.println("Successfully updated work log with id " + worklogId);
            }
            break;
            case "V": {
                int taskId = findTask(isAdmin);
                if (taskId == -1) {
                    return;
                }
                List<WorkingLog> logs = workLogService.findInCommonTable("working_logs", " task_id = " + taskId, "id");
                logs.forEach(l -> System.out.println(l.toString()));
                break;
            }
            case "B":
                return;
        }
    }
    private String createTime() {
        System.out.println("Enter the time you worked on this task...");
        System.out.println("Enter year...");
        int year = Integer.parseInt(sc.nextLine());
        System.out.println("Enter month...");
        int month = Integer.parseInt(sc.nextLine());
        System.out.println("Enter date...");
        int day = Integer.parseInt(sc.nextLine());
        System.out.println("Enter hours you worked on the task...");
        int hours = Integer.parseInt(sc.nextLine());
        System.out.println("Enter minutes...");
        int minutes = Integer.parseInt(sc.nextLine());

        return LocalDateTime.of(year, month, day, hours, minutes).toString().replace("T", " ");
    }

    private int findTask(boolean isAdmin) {
        System.out.println("Enter task id...");
        int taskId = Integer.parseInt(sc.nextLine());
        Task task = taskService.findById(taskId);
        if (task == null) {
            System.out.println("Invalid task id.");
            return -1;
        }
        if (!isAdmin) {
            if (taskValidation.validateUserInTask(UserService.user.getId(), taskId)) {
                if (task.getCreator() != UserService.user.getId()) {
                    System.out.println("This task is not shared with you.");
                    return -1;
                }
            }
        }
        return taskId;
    }
}
