import java.io.*;
import java.util.*;

public class VotingController
{
    private ArrayList staffs = new ArrayList();
    private ArrayList candidates = new ArrayList();

    private Staff theStaff;
    private Candidate theCandidate; 

    public VotingController()
    {
        loadStaffData();
        loadCandidateData();
    }


    //loads candidates from file. This method is complete and working ok.
    public void loadCandidateData()
    {
        try
        {
            String fileName = "candidates.txt";
            File theFile = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(theFile));

            String candidateData;

            while((candidateData = reader.readLine())!= null)
            {
                String[] candidateDetails = candidateData.split(",");
                int code = Integer.parseInt(candidateDetails[0]);
                int votes = Integer.parseInt(candidateDetails[2]);
                theCandidate = new Candidate(code, candidateDetails[1], votes);
                candidates.add(theCandidate);
            }
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("Error! There was a problem with loading candidate names from file");
        }
    }

    //loads staff names from file. This method is complete and working ok.
    public void loadStaffData()
    {
        try
        {
            String fileName = "staff.txt";


            File theFile = new File(fileName);

            BufferedReader reader = new BufferedReader(new  FileReader(theFile));

            String studentData;
            String[] staffDetails;

            while((studentData = reader.readLine()) != null)
            {
                staffDetails = studentData.split(",");
                int id = Integer.parseInt(staffDetails[0]);
                int voted = Integer.parseInt(staffDetails[2]);
                theStaff = new Staff(id, staffDetails[1], voted);
                staffs.add(theStaff);
            }
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("Error! There was a problem with loading staff names from file");
        }
        catch(Exception e)
        {
            System.out.println("Error! Unlown problem accoured during loading the staff names from file.");
        }
    }

    //returns a staff if found in the staffs ArrayList
    public Staff getStaff(int id)
    {
        Iterator it = staffs.iterator();
        while(it.hasNext())
        {
            theStaff = (Staff) it.next();
            if(theStaff.getId()== id)
            {
                return theStaff;
            }
        }
        return null;
    }

    //returns the candidate if found in the candidates ArrayList
    public Candidate getCandidate(int candidateCode)
    {
        Iterator it = candidates.iterator();
        while(it.hasNext())
        {
            theCandidate = (Candidate) it.next();
            if(theCandidate.getCandidateCode() == candidateCode){
                return theCandidate;
            }
        }
        return null;
    }

    //returns the collection of candidates
    public ArrayList getCandidates()
    {
        return candidates;
    }
   
    public ArrayList getStaffs() {
		
		return staffs;
	}


    //returns total number of staffs in the collection
    public int getTotalVoters()
    {
        return staffs.size();
    }


    //every staff vote must be saved to file ??use to add candidate
    public void recordVote()
    {
    	
        theStaff.setVoted();
        theCandidate.addVote();
        saveStaffData();//save to file
        saveCandidateData();//save to file
    }


    //writes staffs back to file
    public void saveStaffData()
    {
        try
        {
            BufferedWriter writer = new  BufferedWriter(new FileWriter("staff.txt"));
            Iterator it = staffs.iterator();
            String staffDetails;
            while(it.hasNext())
            {
                theStaff = (Staff) it.next();
                staffDetails = theStaff.getId() + "," +theStaff.getName() + "," + theStaff.hasVoted() +"\n";
                writer.write(staffDetails);
            }
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }


    //writes candidates back to file ??Similer to add candidate and staff
    // step 1 add , step 2 add function. Writes back to file.
    public void saveCandidateData()
    {
        try
        {
            BufferedWriter writer = new  BufferedWriter(new FileWriter("candidates.txt"));
            Iterator it = candidates.iterator();
            String candidateDetails;
            while(it.hasNext())
            {
                theCandidate = (Candidate) it.next();
                candidateDetails = theCandidate.getCandidateCode() + "," +theCandidate.getName() + "," + theCandidate.getVotes() +"\n";
                writer.write(candidateDetails);
            }
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }


	public void setCandidates() {
			
	}

	public void setStaff() {
			
	}


	public void setCandidates(ArrayList candidates2) {
		
		
	}


	public void setStaff(ArrayList staffs2) {
		
		
	}


	


	
}