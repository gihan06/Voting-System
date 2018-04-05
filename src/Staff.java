public class Staff
{
    private int id; 
    private String name;
    private int voted; //has the staff voted

    public Staff(int id, String name, int voted)
    { 
        this.id = id;
        this.name = name;
        this.voted = voted;
    }

    
    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public void setVoted()
    {
        this.voted = 1;
    }
    
   

    public int hasVoted()
    {
        return voted;
    }


	


	
}
