package com.scalefocus.view;

import com.scalefocus.model.Task;
import com.scalefocus.model.User;
import com.scalefocus.repository.*;
import com.scalefocus.service.*;
import com.scalefocus.util.*;
import java.util.List;

public class TaskView extends View {
    private final TaskService taskService;
    private final Validation.TaskValidation taskValidation;
    private final Validation.ProjectValidation projectValidation;

    public TaskView() {
        this.taskService = new TaskService(new TaskRepository());
        this.taskValidation = new Validation.TaskValidation(taskService);
        this.projectValidation = new Validation.ProjectValidation(new ProjectService(new ProjectRepository()));
    }

    @Override
    public void menu(boolean isAdmin) {
        Printer.taskMenu();
        String option = sc.nextLine().toUpperCase();
        switch (option) {
            case "C":
                int projectId;
                if (!isAdmin) {
                    projectId = getProjectId(UserService.user);
                    if (projectId == -1) {
                        return;
                    }
                } else {
                    projectId = Integer.parseInt(sc.nextLine());
                }
                String[] columns = {"name", "status", "creator_id", "project_id"};
                Object[] values = new Object[4];
                System.out.println("Enter title of the task...");
                values[0] = sc.nextLine();
                values[1] = "PENDING";
                values[2] = UserService.user.getId();
                values[3] = projectId;
                Task created = taskService.save(columns, values);
                System.out.println("Successfully created task with id " + created.getId());
                break;
            case "E": {
                System.out.println("Enter task id...");
                int taskId = Integer.parseInt(sc.nextLine());
                Task task = taskService.findById(taskId);
                if (task == null) {
                    System.out.println("Invalid task id.");
                    return;
                }
                if (!isAdmin) {
                    if (taskValidation.validateUserInTask(UserService.user.getId(), taskId)) {
                        if (task.getCreator() != UserService.user.getId()) {
                            System.out.println("You are not allowed to edit this task.");
                            return;
                        }
                    }
                }
                Printer.editTask();
                String opt = sc.nextLine().toUpperCase();
                String[] column = new String[1];
                Object[] value = new Object[1];
                switch (opt) {
                    case "S":
                        System.out.println("Enter status (PENDING | COMPLETE)...");
                        column[0] = "status";
                        value[0] = sc.nextLine().toUpperCase();
                        break;
                    case "N":
                        System.out.println("Enter new name...");
                        column[0] = "name";
                        value[0] = sc.nextLine();
                        break;
                    case "U":
                        if (task.getCreator() != UserService.user.getId()){
                            System.out.println("Only the creator can add/remove other users.");
                            return;
                        }
                        System.out.println("::A::dd user | ::R::emove user");
                        String wish = sc.nextLine().toUpperCase();
                        System.out.println("Enter user id...");
                        int userId = Integer.parseInt(sc.nextLine());

                        if (wish.equals("A")) {
                            while (taskValidation.validateUserInTask(userId, taskId)) {
                                System.out.println("This user is already assigned to this task. Try again.");
                                userId = Integer.parseInt(sc.nextLine());
                            }
                            taskService.saveInCommonTable("users_tasks",
                                    "user_id, task_id".split(", "), new Object[]{userId, taskId});
                            System.out.println("Successfully added user with id " + userId + " to the task.");
                        } else {
                            while (taskValidation.validateUserInTask(userId, taskId)) {
                                System.out.println("This user is not assigned to this task. Try again.");
                                userId = Integer.parseInt(sc.nextLine());
                            }
                            taskService.deleteInCommonTable("users_tasks", " user_id = " + userId);
                            System.out.println("Successfully removed user with id " + userId + " from the task.");
                        }
                        return;
                }
                taskService.update(column, value, " id = " + taskId);
                System.out.println("Successfully edited task with id " + taskId);
                break;
            }
            case "D":
                System.out.println("Enter task id to delete...");
                int taskId = Integer.parseInt(sc.nextLine());
                Task task = taskService.findById(taskId);
                if (task == null) {
                    menu(false);
                    return;
                }
                if (!isAdmin) {
                    if (task.getCreator() != UserService.user.getId()) {
                        System.out.println("Only the creator of the task can delete it.");
                        return;
                    }
                }
                taskService.delete(taskId);
                taskService.deleteInCommonTable("users_tasks", " task_id = " + taskId);
                System.out.println("Successfully deleted task with id " + taskId);
                break;
            case "V":
                if (!isAdmin) {
                    System.out.println("All tasks in projects...");
                    List<Task> tasks = taskService.findTasksByUserId(UserService.user.getId());
                    tasks.forEach(t -> System.out.println(t.toString()));
                    System.out.println();
                    System.out.println("Tasks shared with you...");
                    List<Task> shared = taskService.findInCommonTable("users_tasks",
                            " user_id = " + UserService.user.getId(), "task_id");
                    shared.forEach(t -> System.out.println(t.toString()));
                    System.out.println();
                }else{
                    taskService.findAll().forEach(t -> System.out.println(t.toString()));
                }
                break;
            case "B":
                return;
        }
    }

    private int getProjectId(User user) {
        System.out.println("Enter project id...");
        int projectId = Integer.parseInt(sc.nextLine());

        while (!projectValidation.validateCreator(projectId, user.getId())) {
            System.out.println("Only the creator of the project may hook up tasks.");
            return -1;
        }
        return projectId;
    }
}
