package taskAllocation;

public class Task {

	private String taskName;
	private int jobNumber;
	private String taskNotes;
	private int taskDuration;
	private boolean taskComplete;
	private int taskUrgency;
	private boolean taskSuspended;
	private String assignedTo;
	
	public Task(String taskName, int jobNumber, String taskNotes, int taskDuration, boolean taskComplete,
			int taskUrgency, boolean taskSuspended, String assignedTo) {
		this.taskName = taskName;
		this.jobNumber = jobNumber;
		this.taskNotes = taskNotes;
		this.taskDuration = taskDuration;
		this.taskComplete = taskComplete;
		this.taskUrgency = taskUrgency;
		this.taskSuspended = taskSuspended;
		this.assignedTo = assignedTo;
		
	}

	public Task(String taskName, int jobNumber, String taskNotes, int taskDuration, boolean taskComplete, boolean taskSuspended,
			String assignedTo) {
		this.taskName = taskName;
		this.jobNumber = jobNumber;
		this.taskNotes = taskNotes;
		this.taskDuration = taskDuration;
		this.taskSuspended = taskSuspended;
		this.taskComplete = taskComplete;
		this.assignedTo = assignedTo;
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

	public String getTaskNotes() {
		return taskNotes;
	}

	public void setTaskNotes(String taskNotes) {
		this.taskNotes = taskNotes;
	}

	public int getTaskDuration() {
		return taskDuration;
	}

	public void setTaskDuration(int taskDuration) {
		this.taskDuration = taskDuration;
	}

	public boolean isTaskComplete() {
		return taskComplete;
	}

	public void setTaskComplete(boolean taskComplete) {
		this.taskComplete = taskComplete;
	}

	public int getTaskUrgency() {
		return taskUrgency;
	}

	public void setTaskUrgency(int taskUrgency) {
		this.taskUrgency = taskUrgency;
	}

	public boolean isTaskSuspended() {
		return taskSuspended;
	}

	public void setTaskSuspended(boolean taskSuspended) {
		this.taskSuspended = taskSuspended;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	
	

}