package utils;

public class Task {

    private String taskName;
    private int jobNumber;
    private String description;
    private int duration;
    private int urgency;
    private int Suspended;
    private String Username;
    private boolean complete;
    private String assignedTo;
    private String taskNotes;

    public Task(String taskName, String taskNotes, int jobNumber, boolean complete, int duration, int urgency, String assignedTo) {
        this.taskName = taskName;
        this.taskNotes = taskNotes;
        this.jobNumber = jobNumber;
        this.duration = duration;
        this.assignedTo = assignedTo;
        this.urgency = urgency;
        this.complete = complete;
    }

    public Task(String taskName, int jobNumber, String description, int duration, int urgency) {
        this.taskName = taskName;
        this.jobNumber = jobNumber;
        this.description = description;
        this.duration = duration;
        this.urgency = urgency;
    }

    public Task(String taskName, String taskNotes, int timeNeeded, String assignedTo, boolean complete) {
        this.taskName = taskName;
        this.taskNotes = taskNotes;
        this.duration = timeNeeded;
        this.assignedTo = assignedTo;
        this.complete = complete;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getJobNumber() {
        return jobNumber;
    }
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUrgency() {
        return urgency;
    }
    public void setUrgency(int urgency) {
        urgency = urgency;
    }

    public int getSuspended() {
        return Suspended;
    }
    public void setSuspended(int suspended) {
        Suspended = suspended;
    }

    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }

    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }


    public String getTaskNotes() {
        return taskNotes;
    }
    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }
}