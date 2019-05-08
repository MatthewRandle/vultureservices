package taskAllocation;

public class Task {

	private String task_name;
	private int job_number;
	private String description;
	private int duration;
	private int Urgency;
	private int Suspended;
	private String Username;
	private boolean complete;
	private String assignedTo;
	private String taskNotes;
	
	public Task(String taskName, String taskNotes, int job_number, boolean complete, int duration, int urgency, String assignedTo) {
		this.task_name = taskName;
		this.taskNotes = taskNotes;
		this.job_number = job_number;
		this.duration = duration;
		this.assignedTo = assignedTo;
		this.Urgency = urgency;
		this.complete = complete;
	}


	
	public Task(String taskName, String taskNotes, int timeNeeded, String assignedTo, boolean complete) {
		this.task_name = taskName;
		this.taskNotes = taskNotes;
		this.duration = timeNeeded;
		this.assignedTo = assignedTo;
		this.complete = complete;
	}

	public String getTask_name() {
		return task_name;
	}




	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}




	public int getJob_number() {
		return job_number;
	}




	public void setJob_number(int job_number) {
		this.job_number = job_number;
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
		return Urgency;
	}




	public void setUrgency(int urgency) {
		Urgency = urgency;
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