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
    private int taskDuration;
    private boolean taskComplete;
    private int taskUrgency;
    private boolean taskSuspended;
	private String dueDate;
	private String assignedDate;
    
	/*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param jobNumber the number of the job the task is within 
	 * @param taskNotes short description of the task 
	 * @param taskDuration the expected duration of the task 
	 * @param taskComplete if the task is completed 
	 * @param taskUrgency a number representing how urgent the task is
	 * @param taskSuspended if the task is suspended or not
	 * @param assignedTo a user name of a technician in which the task is assigned to 
	 * @param dueDate the exact date and time in which the task is due by 
	 * @param assignedDate the exact date and time in which the date was assigned 
	 */
    public Task(String taskName, int jobNumber, String taskNotes, int taskDuration, boolean taskComplete,
			int taskUrgency, boolean taskSuspended, String assignedTo, String dueDate,String assignedDate) {
		this.taskName = taskName;
		this.jobNumber = jobNumber;
		this.taskNotes = taskNotes;
		this.taskDuration = taskDuration;
		this.taskComplete = taskComplete;
		this.setTaskUrgency(taskUrgency);
		this.taskSuspended = taskSuspended;
		this.assignedTo = assignedTo;
		this.setDueDate(dueDate);
		this.setAssignedDate(assignedDate);
		
	}
    /*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param jobNumber the number of the job the task is within 
	 * @param taskNotes short description of the task 
	 * @param taskDuration the expected duration of the task 
	 * @param taskComplete if the task is completed 
	 * @param taskSuspended if the task is suspended or not
	 * @param assignedTo a user name of a technician in which the task is assigned to 
	 * @param dueDate the exact date and time in which the task is due by 
	 * @param assignedDate the exact date and time in which the date was assigned 
	 */
    public Task(String taskName, int jobNumber, String taskNotes, int taskDuration, boolean taskComplete,
                int taskUrgency, boolean taskSuspended, String assignedTo) {
        this.taskName = taskName;
        this.jobNumber = jobNumber;
        this.taskNotes = taskNotes;
        this.taskDuration = taskDuration;
        this.taskComplete = taskComplete;
        this.setTaskUrgency(taskUrgency);
        this.taskSuspended = taskSuspended;
        this.assignedTo = assignedTo;

    }
    /*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param jobNumber the number of the job the task is within 
	 * @param taskNotes short description of the task 
	 * @param taskDuration the expected duration of the task 
	 * @param taskComplete if the task is completed 
	 * @param assignedTo a user name of a technician in which the task is assigned to  
	 */
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
    /*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param jobNumber the number of the job the task is within 
	 * @param taskNotes short description of the task 
	 * @param duration the expected duration of the task 
	 * @param assignedTo a user name of a technician in which the task is assigned to 
	 * @param complete if the task is completed 
	 * @param urgency a number representing how urgent the task is

	  
	 */
    public Task(String taskName, String taskNotes, int jobNumber, boolean complete, int duration, int urgency, String assignedTo) {
        this.taskName = taskName;
        this.taskNotes = taskNotes;
        this.jobNumber = jobNumber;
        this.duration = duration;
        this.assignedTo = assignedTo;
        this.urgency = urgency;
        this.complete = complete;
    }
    /*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param jobNumber the number of the job the task is within 
	 * @param description short description of the task 
	 * @param duration the expected duration of the task 
	 * @param urgency a number representing how urgent the task is
	 
	 */
    public Task(String taskName, int jobNumber, String description, int duration, int urgency) {
        this.taskName = taskName;
        this.jobNumber = jobNumber;
        this.description = description;
        this.duration = duration;
        this.urgency = urgency;
    }
    /*
	 * constructer for a task within the system 
	 * @param taskname the name of the task
	 * @param taskNotes short description of the task 
	 * @param duration the expected duration of the task 
	 * @param complete if the task is completed 
	 * @param assignedTo a user name of a technician in which the task is assigned to 
	 */
    public Task(String taskName, String taskNotes, int timeNeeded, String assignedTo, boolean complete) {
        this.taskName = taskName;
        this.taskNotes = taskNotes;
        this.duration = timeNeeded;
        this.assignedTo = assignedTo;
        this.complete = complete;
    }
    /*
 	* get the name of the task 
 	* 
 	* return taskName 
 	*/
    public String getTaskName() {
        return taskName;
    }
    /*
 	* set the name of the task 
 	* 
 	* @param taskName the name of the task 
 	*/
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    /*
 	* get the job number the task is assigned to
 	* 
 	* return jobNumber  
 	*/
    public int getJobNumber() {
        return jobNumber;
    }
    /*
 	* set the job number the task is assigned to
 	* 
 	* @param jobNumber the job number the task is assigned to  
 	*/
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    /*
 	* get the short description of the task 
 	* 
 	* return description 
 	*/
    public String getDescription() {
        return description;
    }
    /*
 	* set the short description of the task 
 	* 
 	* @param description a short description of the task 
 	*/
    public void setDescription(String description) {
        this.description = description;
    }

    /*
 	* get the expected duration
 	* 
 	* return taskDuration
 	*/
    public int getDuration() {
        return duration;
    }
    /*
 	* set the expected duration
 	* 
 	* @param taskDuration the expected duration of the task 
 	*/
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /*
 	* get the urgency of the task 
 	* 
 	* return urgency
 	*/
    public int getUrgency() {
        return urgency;
    }
    /*
 	* set the name of the task 
 	* 
 	* @param urgency the urgency of the task 
 	*/
    public void setUrgency(int urgency) {
        urgency = urgency;
    }

    /*
 	* get true or false if the task is suspended or not 
 	* 
 	* return taskSuspended
 	*/
    public int getSuspended() {
        return Suspended;
    }
    /*
 	* set true or false if the task is suspended or not 
 	* 
 	* @param taskSuspended true or false if the task is suspended
 	*/
    public void setSuspended(int suspended) {
        Suspended = suspended;
    }

    /*
 	* get the name of the technician in which the task is assigned to 
 	* 
 	* return username
 	*/
    public String getUsername() {
        return Username;
    }
    /*
 	* set the name of the technician in which the task is assigned to 
 	* 
 	* @param username name of the technician being assigned the task  
 	*/
    public void setUsername(String username) {
        Username = username;
    }

    /*
 	* get true or false if the task is complete 
 	* 
 	* return taskComplete
 	*/
    public boolean isComplete() {
        return complete;
    }
    /*
 	* set true or false if the task is complete 
 	* 
 	* @param complete true or false if the task is complete 
 	*/
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /*
 	* get the name of the technician in which the task is assigned to 
 	* 
 	* return assignedTo  
 	*/
    public String getAssignedTo() {
        return assignedTo;
    }
    /*
 	* set the name of the technician in which the task is assigned to 
 	* 
 	* @param assignedTo name of the technician being assigned the task  
 	*/
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /*
	* get the short description of the task 
	* 
	* return taskNotes 
	*/
    public String getTaskNotes() {
        return taskNotes;
    }
/*
	* set the short description of the task 
	* 
	* @param taskNotes a short description of the task 
	*/
    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    /*
 	* get the expected duration
 	* 
 	* return taskDuration
 	*/
    public int getTaskDuration() {
        return taskDuration;
    }
    /*
 	* set the expected duration
 	* 
 	* @param taskDuration the expected duration of the task 
 	*/
    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    /*
 	* get true or false if the task is complete 
 	* 
 	* return taskComplete
 	*/
    public boolean isTaskComplete() {
        return taskComplete;
    }

    /*
 	* set true or false if the task is complete 
 	* 
 	* @param taskComplete true or false if the task is complete 
 	*/
    public void setTaskComplete(boolean taskComplete) {
        this.taskComplete = taskComplete;
    }

    /*
 	* get true or false if the task is suspended or not 
 	* 
 	* return taskSuspended
 	*/
    public boolean isTaskSuspended() {
        return taskSuspended;
    }

    /*
 	* set true or false if the task is suspended or not 
 	* 
 	* @param taskSuspended true or false if the task is suspended
 	*/
    public void setTaskSuspended(boolean taskSuspended) {
        this.taskSuspended = taskSuspended;
    }

    /*
 	* get the exact date and time in which the task is due 
 	* 
 	* return dueDate
 	*/
	public String getDueDate() {
		return dueDate;
	}

	/*
 	* set the exact date and time in which the task is due 
 	* 
 	* @param dueDate the exact date and time in which the task is due 
 	*/
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/*
 	* get the name of the technician in which the task is assigned to 
 	* 
 	* return assignedTo  
 	*/
	public String getAssignedDate() {
		return assignedDate;
	}

	/*
 	* set the name of the technician in which the task is assigned to 
 	* 
 	* @param assignedTo name of the technician being assigned the task  
 	*/
	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	/*
 	* get the urgency of the task 
 	* 
 	* return taskUrgency
 	*/
	public int getTaskUrgency() {
		return taskUrgency;
	}

	/*
 	* set the name of the task 
 	* 
 	* @param taskUrgency the urgency of the task 
 	*/
	public void setTaskUrgency(int taskUrgency) {
		this.taskUrgency = taskUrgency;
	}
}