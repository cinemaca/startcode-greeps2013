import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * @author (your name here)
 * @version 0.1
 */
public class Greep extends Creature
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    /**
     * Default constructor for testing purposes.
     */
    public Greep()
    {
        this(null);
    }

    
    /**
     * Create a Greep with its home space ship.
     */
    public Greep(Ship ship)
    {
        super(ship);
    }
    

    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        if(atShip()){
            //reset
            setMemory(255);
            setFlag(1, true);
            setFlag(2, false);
        }
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
                turn(180);
            }
            else {
                getBack();
            }
            move();//done once while having tomatoes
        }
        else {//not found tomatoes yet
           
            if(getMemory()==200){
                //I was just at a food pile...
                if(!atFoodPile()){
                    //spit("blue");
                    turnHome();
                    rightFace();
                }
            }
            if(atFoodPile())
            {
                setMemory(200);
                //wait and load...
               checkFood(); 
               if(pileEmpty()){
                    //loaded last tomato tell others to go away
                    spit("blue");
                } 
        
            } 
            else
            {
                continueSearch();
                move();//done once while not having tomatoes

            }

        
           
        }
    }
    /**
    * rallys a greep back towards the ship
    * @author Russell Fair
    * @since 0.1
    */
    public void getBack()
    {
        if(atWorldEdge()&&atWater())
            aboutFace();//prevents getting stuck in water+world corners, level 2
        else if(atWorldEdge())
            turnHome();
        else if(seePaint("red"))
            turnHome();

        if(atWater())
            reRoute();
        else
            continueRoute();

    }
    /** routes the greep away from water 
    * @author Russell Fair
    * @since 0.1
    */
    public void reRoute()
    {
        setFlag(1, false);
        setMemory(9);//once rerouted, set it up to do a 10 turn route around water
        turn(30);
    }
    /** continues the route of the greep twards the ship, but away from the water
    * @author Russell Fair
    * @since 0.1
    */    
    public void continueRoute()
    {
        int turns = getMemory();
        if(turns==255){
            turnHome();
        }
        if(turns>4){//go N turns and don't turn...
            setMemory(turns-1);
        }else if(turns>1){
            turn(-10);
            setMemory(turns-1);
        }else if(!getFlag(1)){//then turn some
            turnHome();
            //spit("purple");//was useful in debugging but slowed the greeps down
            //setMemory(7);//do another round or 7 turns @ current direction then turn
            setFlag(1, true);
        }else{//we have gone enough turns
            turnHome();
        }
    }
    /** continues the route of the greep hopefully towards tomatoes
    * @author Russell Fair
    * @since 0.1
    */    
    public void continueSearch()
    {
        if(atWorldEdge()){
            bounce();
        }else if(seePaint("blue")){
            //spit("blue");
            bounce();
        }else if(atWater()){
            //spit("blue");
            setFlag(2, true);//just bounced, zag next time please
            setMemory(10);
            if(getFlag(2)){//should zig
                //turn(20);//work the way through world;
                leftFace();
                setFlag(2, false);  
                setMemory(getMemory()-1);
            }else{//should zag
                //turn(-180);
                //turn(-20);
                leftFace();
                setFlag(2, true);
                setMemory(getMemory()-1);
            }
        }else{//not at water, edge or blue paint
            //hunt();
            if(getMemory()==1){
                reRoute();
                setMemory(10);
            }
                
               
        }
        
    }

    /** checks if greep is loitering at a food pile
    * @author Russell Fair
    * @since 0.1
    */
    public boolean atFoodPile()
    {
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
            if(tomatoes != null) 
                return true;

            return false;
    }

    
    /** checks if the pile is finished
    * @author Russell Fair
    * @since 0.1
    */
    public boolean pileEmpty()
    {
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
            if(tomatoes != null) 
                return false;

            return true;
    }
    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = (TomatoPile) getOneIntersectingObject(TomatoPile.class);
        if(tomatoes != null) {
            giveTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        }
        else{
            spit("blue");
        }
    }


    /**
     * Load a tomato onto *another* creature. This works only if there is another creature
     * and a tomato pile present, otherwise this method does nothing.
     */
    public final void giveTomato()
    {
        loadTomato();
        //if(!seePaint("red"))
        spit("red");
    }

    /**
    * turn around silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void aboutFace()
    {
        turn(180-Greenfoot.getRandomNumber(33));
        //a near total turn around, but not entirely.
    }


    /**
    * turn right silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void rightFace()
    {
        turn(90-Greenfoot.getRandomNumber(10));
        //a near total turn around, but not entirely.
    }

    /**
    * turn left silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void leftFace()
    {
        turn(270-Greenfoot.getRandomNumber(10));
        //a near total turn around, but not entirely.
    }

    /**
    * go away silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void bounce()
    {
       int rando = Greenfoot.getRandomNumber(180);
       turn(rando);
    }


    /**
     * This method specifies the name of the author (for display on the result board).
     */
    public static String getAuthorName()
    {
        return "Russell's Greeps";  // write your name here!
    }


    /**
     * This method specifies the image we want displayed at any time. (No need 
     * to change this for the competition.)
     */
    public String getCurrentImage()
    {
        if(carryingTomato())
            return "greep-with-food.png";
        else
            return "greep.png";
    }
}
