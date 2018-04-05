public class Candidate
{
    int candidateCode = 999;//999 is not an eligible candidate
    String name = null; //candidate name
    int votes = 0; //total votes

    //constructor
    public Candidate(int candidateCode, String name, int votes)
    {
        this.candidateCode = candidateCode;
        this.name = name;
        this.votes = votes;
    } 
    
    
    	
    
    public int getCandidateCode ()
    {
        return candidateCode; 
    }
    

    public String getName()
    {
        return  name;
    }
    public String setName()
    {
        return  name;
    }
    public int getVotes()
    {
        return  votes;
    }

    public void addVote()
    {
        votes++;
    }

	public void setName(String Name) {
		
		
	}

	public void setCandidateCode(int CandidateCode) {
		
		
	}
    

}