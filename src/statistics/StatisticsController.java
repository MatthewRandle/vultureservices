package statistics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import userAccount.UserModalController;
import utils.SceneController;
import utils.Variables;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class StatisticsController implements Initializable { 
	
	public AreaChart<Number, Number> durationChart;
	public HashMap<String, Long> jobDuration;
	public HashMap<String, Long> taskDuration;
	public HashMap<String, Long> estTaskDuration;
	public HashMap<String, Integer> jobDelay;
	public HashMap<String, Integer> inspectionDelay;
	public ArrayList<String> manufacturerList;
	public ArrayList<String> manufacturerInspectionList;
	public ArrayList<String> motorList;
	public String manufacturer;
    Connection connection;
    PreparedStatement ps;

	@FXML Label username;
	@FXML Button notifications, editAccount;
    
    @FXML CategoryAxis xAxis;
    @FXML NumberAxis yAxis;
    @FXML CategoryAxis xAxis_durationVsEst;
    @FXML NumberAxis yAxis_durationVsEst;
    @FXML CategoryAxis xAxis_unexpectedDelay;
    @FXML NumberAxis yAxis_unexpectedDelay;
    @FXML CategoryAxis xAxis_finalInspectionFailureChart;
    @FXML NumberAxis yAxis_finalInspectionFailureChart;
    
    
    @FXML BarChart<String, Number> jobTaskDurationChart;
    @FXML BarChart<String, Number> estActualTaskDurationChart;
    @FXML BarChart<String, Number> unexpectedDelayChart;
    @FXML BarChart<String, Number> finalInspectionFailureChart;
	Series<String, Number> series1, series2, series3, series4, series5, series6;
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	jobTaskDurationChart.setTitle("Job Duration vs Est. Task Duration");  
    	estActualTaskDurationChart.setTitle("Estimated Task Duration vs Actual Task Duration"); 
    	unexpectedDelayChart.setTitle("Frequency of Unexpected Delays due to Ordering Parts");
    	finalInspectionFailureChart.setTitle("Frequency of Unexpected Delays due to Inspection Failure");

    	manufacturerList = new ArrayList<String>();
    	manufacturerInspectionList = new ArrayList<String>();
    	motorList = new ArrayList<String>();
    	jobDuration = new HashMap<String, Long>();
    	taskDuration = new HashMap<String, Long>();
    	estTaskDuration = new HashMap<String, Long>();
    	jobDelay = new HashMap<String, Integer>();
    	inspectionDelay = new HashMap<String, Integer>();
    	
    	try {
			compareJobDuration();
			compareTaskDuration();
			compareEstimatedTaskDuration();
			unexpectedDelayFrequency();
			finalInspectionFailure();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	getManufacturer();
    	xAxis.setCategories(FXCollections.<String>observableArrayList(manufacturerList)); 
    	xAxis.setLabel("Manufacturer");    			
    	yAxis.setLabel("Duration");
    	
    	xAxis_durationVsEst.setCategories(FXCollections.<String>observableArrayList(manufacturerList)); 
    	xAxis_durationVsEst.setLabel("Manufacturer");    			
    	yAxis_durationVsEst.setLabel("Duration");
    	
    	xAxis_unexpectedDelay.setCategories(FXCollections.<String>observableArrayList(motorList)); 
    	xAxis_unexpectedDelay.setLabel("Motor");    			
    	yAxis_unexpectedDelay.setLabel("Frequency");
    	
    	xAxis_finalInspectionFailureChart.setCategories(FXCollections.<String>observableArrayList(manufacturerInspectionList)); 
    	xAxis_finalInspectionFailureChart.setLabel("Manufacturer");
    	yAxis_finalInspectionFailureChart.setLabel("Frequency");
    	
  
    	populateJobDuration();
    	populateTaskDuration();
    	populateEstTaskDuration();
    	populateJobDelay();
    	populateInspectionFailure();
		jobTaskDurationChart.getData().addAll(series1, series2);
		estActualTaskDurationChart.getData().addAll(series3, series4);
		unexpectedDelayChart.getData().addAll(series5);
		finalInspectionFailureChart.getData().addAll(series6);

    }
    
    public void getManufacturer() {
    	try {
    	connection = Variables.getConnection();
        
        String query = "SELECT DISTINCT manufacturer FROM jobs;";
        Statement st = connection.createStatement();
        ResultSet results = st.executeQuery(query);
        
        while (results.next()) {
        	String manufacturer = results.getString("manufacturer");
        	if (manufacturer != null) {
            	manufacturerList.add(manufacturer);
        	}
    }
        }
        catch(SQLException error) {
            System.out.println("Error logging user in");
            System.out.println(error);
        }
    }
        
    
    public void compareJobDuration()  {
    	try {
            connection = Variables.getConnection();
         
            String query = "SELECT arrival_date, return_date, manufacturer FROM jobs;";
            Statement st = connection.createStatement();
            ResultSet results = st.executeQuery(query);
            
            while (results.next()) {
            	Date arrivalDate = results.getDate("arrival_date");
            	Date returnDate = results.getDate("return_date");
            	manufacturer = results.getString("manufacturer");

				if(arrivalDate != null && returnDate != null) {
					if(jobDuration.containsKey(manufacturer)) {
						long currentDiff = jobDuration.get(manufacturer);
						long diffInMillies = Math.abs(returnDate.getTime() - arrivalDate.getTime());
						long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
						jobDuration.put(manufacturer, diff + currentDiff);
					}
					else {
						long diffInMillies = Math.abs(returnDate.getTime() - arrivalDate.getTime());
						long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
						System.out.println(manufacturer + ": " + diff);
						jobDuration.put(manufacturer, diff);
					}
				}
            }
    	}
        catch(SQLException error) {
			System.out.println("Error logging user in");
			System.out.println(error);
		}
	}
            
	public void compareTaskDuration() throws ParseException {
		try {
		String query1 = "SELECT date_assigned, date_completed, manufacturer FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE manufacturer IS NOT NULL;";
		Statement st1 = connection.createStatement();
		ResultSet results1 = st1.executeQuery(query1);

		int index = 0;
		int total = 0;

		while (results1.next()) {
			String dateAssignedString = results1.getString("date_assigned");
			String dateCompletedString = results1.getString("date_completed");
			String manufacturer = results1.getString("manufacturer");

			if(dateCompletedString != null && dateAssignedString != null) {
				Date dateAssigned = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(dateAssignedString);
				Date dateCompleted = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(dateCompletedString);
				long diffInMillies = Math.abs(dateCompleted.getTime() - dateAssigned.getTime());
				long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

				if(taskDuration.containsKey(manufacturer)) {
					long currentDuration = taskDuration.get(manufacturer);
					currentDuration += diff;

					taskDuration.put(manufacturer, currentDuration);
				}
				else {
					taskDuration.put(manufacturer, diff);
				}
			}
		}
	} catch(SQLException error) {
		System.out.println("Error logging user in");
		System.out.println(error);
	}
	}

	public void compareEstimatedTaskDuration() throws ParseException {
		try {
		String query1 = "SELECT date_assigned, date_due, manufacturer FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE manufacturer IS NOT NULL;";
		Statement st1 = connection.createStatement();
		ResultSet results1 = st1.executeQuery(query1);

		int index = 0;
		int total = 0;

		while (results1.next()) {
			String dateAssignedString = results1.getString("date_assigned");
			String dateDueString = results1.getString("date_due");
			String manufacturer = results1.getString("manufacturer");

			if(dateDueString != null && dateAssignedString != null) {
				Date dateAssigned = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(dateAssignedString);
				Date dateDue = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(dateDueString);
				long diffInMillies = Math.abs(dateDue.getTime() - dateAssigned.getTime());
				long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

				if(estTaskDuration.containsKey(manufacturer)) {
					long currentDuration = estTaskDuration.get(manufacturer);
					currentDuration += diff;

					estTaskDuration.put(manufacturer, currentDuration);
				}
				else {
					estTaskDuration.put(manufacturer, diff);
				}
			}
		}
	} catch(SQLException error) {
		System.out.println("Error logging user in");
		System.out.println(error);
	}
	}

	public void unexpectedDelayFrequency() throws ParseException {
		try {
		String query = "SELECT motor, frequency FROM order_parts;";
		Statement st = connection.createStatement();
		ResultSet results = st.executeQuery(query);


		while (results.next()) {
			int frequency = results.getInt("frequency");
			String motor = results.getString("motor");
			if(motor != null) {
				motorList.add(motor);
				jobDelay.put(motor, frequency);
			}
		}
	} catch(SQLException error) {
		System.out.println("SQL Error");
		System.out.println(error);
	}
	}


	public void finalInspectionFailure() {
		try {
			String query = "SELECT manufacturer, frequency FROM inspection_failure;";
			Statement st = connection.createStatement();
			ResultSet results = st.executeQuery(query);

			while (results.next()) {
				int frequency = results.getInt("frequency");
				String manufacturer = results.getString("manufacturer");
				if(manufacturer != null) {
					manufacturerInspectionList.add(manufacturer);
					inspectionDelay.put(manufacturer, frequency);
				}
			}
		} catch(SQLException error) {
			System.out.println("SQL Error");
			System.out.println(error);
		}
	}

	public void populateInspectionFailure() {
		series6 = new Series<>();
		series6.setName("Frequency of Unexpected Delays");

		for (Entry<String, Integer> inspectionDelay : inspectionDelay.entrySet()) {
			series6.getData().add(new XYChart.Data<>(inspectionDelay.getKey(), inspectionDelay.getValue()));
		}
	}


	public void populateJobDuration() {
		series1 = new Series<>();
		series1.setName("Job");

		for (Entry<String, Long> jobDuration : jobDuration.entrySet()) {
			System.out.println(jobDuration.getValue());
			series1.getData().add(new XYChart.Data<>(jobDuration.getKey(), jobDuration.getValue()));
		}
	}

	public void populateTaskDuration() {
		series2 = new Series<>();
		series2.setName("Task Duration");
		series4 = new Series<>();
		series4.setName("Task Duration");

		for (Entry taskDuration : taskDuration.entrySet()) {
			series2.getData().add(new XYChart.Data<>(taskDuration.getKey().toString(), Integer.parseInt(taskDuration.getValue().toString())));
			series4.getData().add(new XYChart.Data<>(taskDuration.getKey().toString(), Integer.parseInt(taskDuration.getValue().toString())));
		}
	}

	public void populateEstTaskDuration() {
		series3 = new Series<>();
		series3.setName("Estimated Task Duration");

		for (Entry estTaskDuration : estTaskDuration.entrySet()) {
			series3.getData().add(new XYChart.Data<>(estTaskDuration.getKey().toString(), Integer.parseInt(estTaskDuration.getValue().toString())));
		}
	}

	public void populateJobDelay() {
		series5 = new Series<>();
		series5.setName("Frequency of Unexpected Delays");

		for (Entry<String, Integer> jobDelay : jobDelay.entrySet()) {
			series5.getData().add(new XYChart.Data<>(jobDelay.getKey().toString(), jobDelay.getValue()));
		}
	}

	public void editSelf() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/userAccount/UserModal.fxml"));
			Parent root = loader.load();

			UserModalController userModalController = loader.getController();
			userModalController.setUsername(Variables.getUserName());
			userModalController.getUserID();
			userModalController.getPassword();
			userModalController.setEditSelfModal();
			userModalController.getUserID();

			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
		catch(IOException err) {
			err.printStackTrace();
		}
	}

	public void checkNotifications() {
		try {
			String query = "select * from alerts JOIN user_types ON user_types.id = alerts.user_type WHERE user_types.type = ?;";
			ps = connection.prepareStatement(query);
			ps.setString(1, Variables.getUserType());
			ResultSet results = ps.executeQuery();
			boolean haveAlerts = results.next();

			if(haveAlerts) {
				notifications.setDisable(false);
				notifications.setVisible(true);
				notifications.setManaged(true);
			}
			else {
				notifications.setDisable(true);
				notifications.setVisible(false);
				notifications.setManaged(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showNotifications() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/userAccount/Notifications.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();

			checkNotifications();
		}
		catch(IOException err) {
			err.printStackTrace();
		}
	}

	public void logout() {
		SceneController.activate("login");
	}
}
