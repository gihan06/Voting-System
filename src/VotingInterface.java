
import java.io.*;
import java.util.*;
import java.sql.Savepoint;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class VotingInterface
{
    
	//  private int studID;
    private VotingController vc;
    private Staff theStaff;
    private Candidate theCandidate;
    private final String USERNAME = "admin";
    private final String PASSWORD ="123";
    private int numberOfCandidates = 0;
    public static Date StartDate= null;
    public static Date EndDate= null;
   
    private BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

    Scanner input = new Scanner(System.in);
    
    public static void main(String[] args)
    {
        VotingInterface vi = new VotingInterface();
        vi.start();
    }
public VotingInterface()
{
	Calendar c1 = Calendar.getInstance();
	c1.set(2015,10,5);
	StartDate=c1.getTime();
	c1.add(c1.DATE, 6);
	EndDate= c1.getTime();
	
}

    public void start()
    {
        vc = new VotingController();
        commenceVoting();
    }


    public void commenceVoting()
    { 
        boolean systemQuit = false;
        while (!systemQuit)
        {
            String input = null;
            System.out.println("\n\t\t============== eVoting System =====================\n\n");
            System.out.print("Enter \"v\" to Vote as staff \nOR \"a\" to login in as system administrator : ");
            input = getInput();

            if (input.equalsIgnoreCase("V"))
            {
                manageVote();
            }
            else if (input.equalsIgnoreCase("A"))
            {
                validateAdmin();
                systemQuit = manageAdmin();
            }
            else
            {
                System.out.println("Your input was not recognised");
            }
        }
    }

    //screen input reader
    private String getInput()
    {
        String theInput = "";

        try
        {
            theInput = in.readLine().trim();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        return theInput;
    }


    public void manageVote()
    {
        boolean moveOn = false;
        if(new Date().compareTo(StartDate) >=0 && new Date().compareTo(EndDate)<=0)
        {
       

        //loop for each voter
        while (moveOn == false)
        {
            System.out.print("Please enter your staff ID :");
            try
            {
                theStaff = vc.getStaff(Integer.parseInt(getInput()));

                if(theStaff.hasVoted() == 1)
                {
                    System.out.println("\nYou have voted and cannot vote again\nGood bye...!");
                    moveOn = true;
                }
                else if (theStaff.hasVoted() == 0)
                {
                    getStaffVote();
                    moveOn = true;
                }
                else
                {
                    System.out.println("There seems to be a problem.  Contact your administrator");
                }
            }

            catch(NumberFormatException e)
            {
                System.out.println("Invalid entry - you must enter a number\nPlease try again");
            }
            catch(NullPointerException e)
            {
                System.out.println("Error! Staff ID not found.\nPress ENTER to try again or  \"q\" to QUIT :  ");
                if ("q".equalsIgnoreCase(getInput()))
                {
                    System.out.println("Good bye!");
                    moveOn = true;
                }
            }
        }
        System.out.print("going back to voting screen...");
        }
        else {
        	System.out.println("Voting system is Closed Now");
        }
    }


    public void displayVotingScreen()
    {

        System.out.println("\nWelcome "+ theStaff.getName()+"!\n");
        numberOfCandidates = 0;

        ArrayList candidates = vc.getCandidates();

        Iterator it = candidates.iterator();
        System.out.println("\tCode\tCandidate Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        while(it.hasNext())
        {   theCandidate = (Candidate)it.next();
            System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getVotes());
            numberOfCandidates++;
        }
    }

    //manages a student's vote
    private void getStaffVote()
    {
        int candidateCode;
        boolean retry = true;
 
        displayVotingScreen();

        while (retry)
        {
            System.out.print("\n\nEnter your candidate's code OR enter Q to quit voting : ");
            try{
                String input = getInput();

                if (input.equalsIgnoreCase("Q"))
                {
                    retry = false;
                }
                else
                {
                    candidateCode = Integer.parseInt(input);
                    theCandidate = vc.getCandidate(candidateCode);
                    System.out.print("\nYou have selected " + theCandidate.getName()+ ". \n\nEnter  Y to confirm or any other key to Cancel, then press ENTER : ");

                    if (getInput().equalsIgnoreCase("y"))
                    {
                        vc.recordVote();
                        System.out.println("\n\nThanks for voting " + theStaff.getName() + ". Bye!!!");
                        retry = false;
                    }
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("That was not a number you entered\nPlease try again");
            }
            catch(NullPointerException e)
            {
                System.out.println("This candidate code does not exit\nPlease try again");
            }
            catch(Exception e){
                System.out.println("We have a problem, please contact your administrator");
            }
        }
    }

    private void validateAdmin()
    {
        boolean adminQuit = false;


        while (!adminQuit){
            System.out.print("\nYou have entered Administration space. \nEnter username or \"Q\" to quit : " );
            String input = getInput();
            if (input.equalsIgnoreCase("q")){ //quit voting
                adminQuit = true;
            }
            else{
                
                String username, password = null;
                username = input.trim();
                System.out.print("\nPlease enter password : ");
                password = getInput().trim();

                if(validateAdmin(username, password)){ //validate admin
                    //clearScreen();    - only work in BlueJ, cannot use this method in Windows Console
                    
                    adminQuit = true;
                }
                else{
                    System.out.println("Incorrect username/password.");
                }
            }
        }
    }

    private boolean manageAdmin()
    {
        boolean adminQuit = false;
        boolean systemQuit = false;

        while (!adminQuit){
            System.out.println("\nTo continue voting enter \"V\":\nTo end voting enter \"Stop\": \nUpadte Candidate \"C\":\nUpadte Staff \"S\" \nAdd Date Range \"D\":\nView Date Range \"R\": \nView Voting Statistics \"M\"  ");
            String input = getInput();
            if (input.equalsIgnoreCase("V")){
                //back to voting
                adminQuit = true;
            }
            else if(input.equalsIgnoreCase("Stop")){
                //stop system
                adminQuit = true;
                systemQuit = true;
                System.out.println("Voting System Closed");
            }
            else if(input.equalsIgnoreCase("C")){             
                adminQuit = false;
                systemQuit = false;
                updateCandidate();               
            }
            else if(input.equalsIgnoreCase("S")){               
                adminQuit = false;
                systemQuit = false;
                updateStaff();                
            }
            else if(input.equalsIgnoreCase("D")){
            	adminQuit = false;
                systemQuit = false;
                AddDateRange();
            }
            else if(input.equalsIgnoreCase("R")){
            	adminQuit = false;
                systemQuit = false;
                ViewDateRange();
            }
            else if(input.equalsIgnoreCase("M")){
            	adminQuit = false;
                systemQuit = false;
                printVoteResults();
            }
            else{
                System.out.println("Cannot understand your input, please re-enter : \n\n");
            }
        }
        return systemQuit;
    }

    
    
    public void updateCandidate()
    {
        boolean systemQuit = false;
        while (!systemQuit) 
        {
            String input = null;
            System.out.println("\n\t\t============== Update Candidate =====================\n\n");
            System.out.print("Enter \"A\" to Add a Candidate \n\"V\" to View Candidate \n\"S\" to Search Candidate \n\"U\" to Update Candidate \n\"D\" to Delete Candidate :");
            input = getInput();

            if (input.equalsIgnoreCase("A"))
            {
                manageVote();
            }
            else if (input.equalsIgnoreCase("V"))
            {
            	viewCandidates();
            	
            }
            else if (input.equalsIgnoreCase("S"))
            {
            	
            	searchCandidates();
            }
            else if (input.equalsIgnoreCase("U"))
            {
            	
            	 updateAdminCandidate();
            }
            else if (input.equalsIgnoreCase("D"))
            {
            	
            	 deleteCandidate();
            }
            else
            {
                System.out.println("Your input was not recognised");
            }
            
        }
    }
    
    public void viewCandidates()
    {
        numberOfCandidates = 0;

        ArrayList candidates = vc.getCandidates();

        Iterator it = candidates.iterator();
        System.out.println("\tCode\tCandidate Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        while(it.hasNext())
        {   theCandidate = (Candidate)it.next();
            System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getVotes());
            numberOfCandidates++;
        }
        updateCandidate();  
    }
    
    public void viewAdminCandidates(){
    	
    	{

            ArrayList candidates = vc.getCandidates();

            Iterator it = candidates.iterator();
            System.out.println("\tSNo\tCode\tCandidate Name\tVotes");
            System.out.println("\t====\t====\t==============\t====\n");
            
            int SNo=1;
            while(it.hasNext())
            {   theCandidate = (Candidate)it.next();
                System.out.println("\t"+SNo+"\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getVotes());
                SNo++;
            }
              
        }
    	
    }
    
    public void addCandidate(int candidateCode,String name )
    {
    	ArrayList<Candidate> candidates = vc.getCandidates();	
		
		theCandidate = new Candidate(candidateCode, name, 0);
		candidates.add(theCandidate);
		
		int i; 
    	boolean xz = false;
    	
    	
    	for(i = 0; i < candidates.size(); i++){
            if(candidates.get(i).getCandidateCode() == candidateCode)
            {
                    xz = true;
                    break; }}
    	if (xz) {
    		Candidate cx = new Candidate(candidateCode, name, 0 );
    		candidates.add(theCandidate);
    	} else {
    		System.out.println("Please enter a valied ID");
    	}
		vc.saveCandidateData();
    	System.out.println("The new candidate has been saved.");
    }
    
    public void updateAdminCandidate()
    {
    System.out.println("Choose the candidate SNo u want to update.");
    viewAdminCandidates();
    ArrayList candidates = vc.getCandidates();
    int choice =input.nextInt();
    theCandidate=(Candidate)candidates.get(choice-1);
    System.out.println("What do you want to update");
    System.out.println("1.Candidate ID");
    System.out.println("2.Candidate Name");
    
    int choice2 = input.nextInt();
    switch (choice2)
    { 
    case 1:
    	System.out.println("Enter the Candidate code:");
    	theCandidate.setCandidateCode(input.nextInt());
    	break;
    case 2:
    	System.out.println("Enter the Candidate name :");
    	theCandidate.setName(getInput());
    	break;
    	default:
    		System.out.println("Please eneter a valid input");
    		updateAdminCandidate();
    		break;
    }
    
    System.out.println("Candidates Updated");
    candidates.set(choice-1,theCandidate);
    vc.saveCandidateData();
    vc.setCandidates(candidates);
    
    }
    
    public void deleteCandidate(){	
    	
    	viewdeletedCandidates();
    	System.out.println("Please choose which candidate you want to delete:");
    	int choice = input.nextInt();
    	ArrayList<Candidate> candidates = vc.getCandidates();	
    	int i;
    	Boolean full = false;
    	
    	
    	for(i = 0; i < candidates.size(); i++){
            if(candidates.get(i).getCandidateCode() == choice){
                    full = true;
                    break; }}
    	if (full) {
    		candidates.remove(i);
    	} else {
    		System.out.println("Please enter a valied ID");
    	}
    	
    	vc.saveCandidateData();
    	System.out.println("Candidate deleted Successfully.");
    }
    
    public void viewdeletedCandidates()
    {
        numberOfCandidates = 0;

        ArrayList candidates = vc.getCandidates();

        Iterator it = candidates.iterator();
        System.out.println("\tCode\tCandidate Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        while(it.hasNext())
        {   theCandidate = (Candidate)it.next();
            System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getVotes());
            numberOfCandidates++;
        }
    }
    
    public void searchCandidates()
    {
    Scanner input = new Scanner(System.in);
    ArrayList candidates = vc.getCandidates();
    Boolean ispresent = false;
    System.out.println("Enter the ID of Candidate you want to search");
    int searchId = input.nextInt();
    
    Iterator it = candidates.iterator();
    
    while(it.hasNext())
    {  
    	theCandidate = (Candidate)it.next();
    	if (theCandidate.getCandidateCode() == searchId)
    
    {
    	System.out.println("\tCode\tCandidate Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getVotes());
        ispresent = true;
        break;
    }
    }
    if (ispresent== false)
    	System.out.println("The Candidate ID you have entered is wrong. Enter a valied Candidate ID");
    	}
    
    
    
    
    public void updateStaff()
    {
        boolean systemQuit = false;
        while (!systemQuit)
        {
            String input = null;
            System.out.println("\n\t\t============== Update Staff =====================\n\n");
            System.out.print("Enter \"A\" to Add a Staff \n\"V\" to View Staff \n\"S\" to Search Staff \n\"U\" to Update Staff \n\"D\" to Delete Staff :");
            input = getInput();

            if (input.equalsIgnoreCase("A"))
            {
                manageVote();
            }
            else if (input.equalsIgnoreCase("V"))
            {
            	viewStaff();
            }
            
            else if (input.equalsIgnoreCase("U"))
            {
            	
            	updateAdminStaff();
            }
            else if (input.equalsIgnoreCase("D"))
            {
            	
            	 deleteStaff();
            }
            else if (input.equalsIgnoreCase("s"))
            {
            	
            	searchStaff();
            }
            else
            {
                System.out.println("Your input was not recognised");
            }
        }
    }
    
    
    public void updateAdminStaff()
    {
    	
    System.out.println("Choose the staff SNo. u want to update.");
    viewAdminStaff();
    ArrayList staffs = vc.getStaffs();
    int choice =input.nextInt();
    theStaff=(Staff)staffs.get(choice-1);
    System.out.println("What do you want to update");
    System.out.println("1.Staff ID");
    System.out.println("2.Staff Name");
    int choice2 = input.nextInt();
    switch (choice2)
    {
    case 1:
    	System.out.println("Enter the Staff code:");
    	theStaff.setId(input.nextInt());
    	break;
    case 2:
    	System.out.println("Enter the name :");
    	theStaff.setName(getInput());
    	break;
    	default:
    		System.out.println("Please eneter a valid input");
    		updateAdminStaff();
    		break;
    }
    
    System.out.println("Staff Updated");
    staffs.set(choice-1,theStaff);
    vc.setStaff(staffs);
    vc.saveStaffData();
    }
    
    
    
    
    
    public void deleteStaff(){
    	viewdeleteStaff();
    	System.out.println("Please choose which Staff ID you want to delete:");
    	int choice = input.nextInt();
    	ArrayList<Staff> Staffs = vc.getStaffs();	
    	int i;
    	Boolean full = false;
    	
    	
    	for(i = 0; i < Staffs.size(); i++){
            if(Staffs.get(i).getId() == choice){
                    full = true;
                    break; }}
    	if (full) {
    		Staffs.remove(i);
    	} else {
    		System.out.println("Please enter a valied ID");
    	}
    	
    	vc.saveCandidateData();
    	System.out.println("Staff deleted Successfully.");
    }
    
  
    
   
    
    public void viewdeleteStaff()
    {
        

        ArrayList staffs = vc.getStaffs();

        Iterator it = staffs.iterator();
        System.out.println("\tCode\tStaff Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        while(it.hasNext())
        {   theStaff = (Staff)it.next();
            System.out.println("\t" + theStaff.getId() + "\t" + theStaff.getName() + "\t" + theStaff.hasVoted());
            
        }
    }
    
    
    
    public void viewStaff()
    {
        

        ArrayList staffs = vc.getStaffs();

        Iterator it = staffs.iterator();
        System.out.println("\tCode\tStaff Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        int sno=1;
        while(it.hasNext())
        {   theStaff = (Staff)it.next();
            System.out.println("\t"+ theStaff.getId() + "\t" + theStaff.getName() + "\t" + theStaff.hasVoted());
            
            sno++;
        }
        updateStaff();  
    }
    
    public void viewAdminStaff()
    {
        

        ArrayList staffs = vc.getStaffs();

        Iterator it = staffs.iterator();
        System.out.println("\tSNo\tCode\tStaff Name\tVotes");
        System.out.println("\t====\t====\t==============\t====\n");
        int SNo=1;
        while(it.hasNext())
        {   theStaff = (Staff)it.next();
            System.out.println("\t"+SNo + "\t"+theStaff.getId() + "\t" + theStaff.getName() + "\t" + theStaff.hasVoted());
            
            SNo++;
        }
         
    }
    
    
    
    
    public void searchStaff()
    {
    Scanner input = new Scanner(System.in);
    ArrayList staffs = vc.getStaffs();
    Boolean ispresent = false;
    System.out.println("Enter the ID of Staff ID you want to search");
    int searchId = input.nextInt();
    
    Iterator it = staffs.iterator();
    
    while(it.hasNext()) 
    {  
    	theStaff = (Staff)it.next();
    	if (theStaff.getId() == searchId)
    
    {
    	System.out.println("\tCode\tStaff Name\tVotes");
        System.out.println("\t====\t==============\t====\n");
        System.out.println("\t" + theStaff.getId() + "\t" + theStaff.getName() + "\t" + theStaff.hasVoted());
        ispresent = true;
        break;
    }
    } 
    if (ispresent== false)
    	System.out.println("The Staff ID you have entered is wrong. Enter a valied Staff ID");
    	}
    
    
    
    
    
    //prints out the voting results
    public void printVoteResults()
    {
        ArrayList candidates = vc.getCandidates();
        int totalVoters = vc.getTotalVoters();
        double totalVoted = 0;
        int candidateVotes = 0;

        //formatting display
        DecimalFormat df = new DecimalFormat("###.##");

        Iterator it = candidates.iterator();
        System.out.println("\n\t\t VOTING STATISTICS");
        System.out.println("\t\t=========================\n");
        System.out.println("Code\tName\t\tVotes\t(Vote%)\tDate");
        System.out.println("____\t____\t\t_____\t______\t_____\n");


        //calculate total voted
        while(it.hasNext()) {
            theCandidate = (Candidate) it.next();
            totalVoted += theCandidate.getVotes();// count total votes for this candidate
        }

        it = candidates.iterator();
        while(it.hasNext()) {
            theCandidate = (Candidate) it.next();
            candidateVotes = theCandidate.getVotes();
            System.out.println(theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" +
                    candidateVotes +"\t(" + df.format((candidateVotes/totalVoted)*100) +"%)"   );
        }
        System.out.println("\nNumbers on voting list: " + totalVoters);
        System.out.println("Numbers voted: " + totalVoted + "(" + df.format((totalVoted/totalVoters)*100)  + "%)");
        System.out.println();

    }


    //validates administrator user-name & password
    public boolean validateAdmin(String username, String password)
    {
        if(username.equalsIgnoreCase(USERNAME)&&(password.equals(PASSWORD))){
            return true;
        }
        else{
            return false;
        }
    }
    public void AddDateRange()
    {
    	String Format = "dd/MM/yyyy";
    	System.out.println(" Enter the date shown in the format ("+Format+")");
    	SimpleDateFormat f1 = new SimpleDateFormat(Format);
    	String StartDateString = input.nextLine();
    	try {
			StartDate = f1.parse(StartDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Calendar c1 = Calendar.getInstance();
    	c1.add(c1.DATE, -1);
    	int x= StartDate.compareTo(c1.getTime());
    	if(x<0)
    	{
    		System.out.println("enter future date only");
    	}
    	c1.setTime(StartDate);
    	c1.add(c1.DATE, 6);
    	EndDate= c1.getTime();
    	System.out.println("Date Range is set Successfully");
    	
    }
    public void UpdateDateRange(){
    	AddDateRange();
    }
    public void ViewDateRange()
    {
    	String EndDateString = "dd/MM/yyy";
    	SimpleDateFormat f1 = new SimpleDateFormat(EndDateString);
    	if (StartDate!=null && EndDate!=null);{
    			
    	System.out.println("The Date Range For Voting Is From " +f1.format(StartDate)+ " To " +f1.format(EndDate) );
    	}
    }
    	
    }


