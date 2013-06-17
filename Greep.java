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
    * Determines if the greep has previously bounced back
    * @author Russell Fair
    * @since 0.3
    */
    public boolean hasBouncedBack(){
        return getFlag(1);
    }

    /**
    * Determines if the greep has previously bounced back
    * @author Russell Fair
    * @since 0.3
    */
    public boolean isBackwardFacing(){
        return getFlag(2);
    }

    /**
    * sets the "has bounced" flag
    * @author Russell Fair
    * @since 0.3
    */
    public void setBounced(){
        setFlag(1, true);
    }
    /**
    *sets the "is backwards" flag
    * @author Russell Fair
    * @since 0.3
    */
    public void setBackwards(){
        setFlag(2, true);
    }

    /**
     * Do what a greep's gotta do.
    * @author Russell Fair
    * @since 0.1
    */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        if(atShip()){
            //reset
            setBrain();
        }
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
                turn(180);
                resetBrain();
            }
            else {
                getBack();
            }
            move();//done once while having tomatoes
        }
        else {//not found tomatoes yet
        
            if(!atFoodPile()){
                goSeek();
                move();
            }
            else //at food pile...
            {
                //wait? 
                turnHome();
                checkFood();
                resetBrain();
                
                if(pileEmpty()){
                    turn(180);
                   
                    goSeek();
                    move();
                }
                   
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
        //handle the at world edge & touching water (corners) first
        if(atWorldEdge()&&atWater()){
            if(!hasBouncedBack()){
                //has never bounced off edge before
                setBounced();
                reRoute();
            }
            else{
                //has bounced before this trip, make backwards facing greep
                setBackwards();
                reRoute();
            }
        }else if(atWorldEdge()){
            setBounced();
            reRoute();
        }

        else if (atWater()){//I'm touching water, but not the edge
            setMemory(199);
            reRoute();
        }
        else {
            //not at water, or edge

        }
        continueHome();


    }
    /**
    * makes the greep seek tomatoes
    * @author Russell Fair
    * @since 0.3
    */
    public void goSeek()
    {
        //handle the at world edge & touching water (corners) first
        if(atWorldEdge()&&atWater()){
            if(!hasBouncedBack()){
                //has never bounced off edge before, bounce right first
                setBounced();
            }
            else{
                //has bounced before this trip, make backwards facing greep
               setBackwards();
            }
            reRoute();
        }
        else if(atWorldEdge()){ 
            if(!hasBouncedBack()){
                setBounced();
            }
            reRoute();
        }
        else if (atWater()){//I'm touching water, but not the edge
            if(!hasBouncedBack()){
                setBackwards();
            }else if(!isBackwardFacing()){
                setBackwards();
            }
            reRoute();
        }
        else if(seePaint("blue")){
            //not at water, or edge but touching red paint trail
            turnHome();
            turn(180);
        }

        continueSearch();


    }
    /**
    * sets the greeps memory back to "nothing"
    * @author Russell Fair
    * @since 0.1
    */
    public void resetBrain()
    {
        setMemory(255);
        setFlag(1, false);
        setFlag(2, false);
    }

    /**
    * sets the greeps initial memory to "nothing"
    * @author Russell Fair
    * @since 0.1
    */
    public void setBrain()
    {
        setMemory(250);
        setFlag(1, false);
        setFlag(2, false);
    }
    /**
    * go around something, to the right
    * @author Russell Fair
    * @since 0.2
    */
    public void goRight()
    {
       turn(-90);
    }

    /**
    * go around something, to the left
    * @author Russell Fair
    * @since 0.2
    */
    public void goLeft()
    {
       turn(90);
    }

    /**
    * reroutes the greep towards home
    * @author Russell Fair
    * @since 0.1
    */
    public void reRoute()
    {
        //needs to do something
        if(isBackwardFacing())
            bounceLeft();
        else
            bounceRight();
    }

    /** 
    * continues the route of the greep towards the ship, but away from the water
    * @author Russell Fair
    * @since 0.1
    */    
    public void continueHome()
    {
        int runtype = getMemory();
        
        if(runtype==255){
            turnHome();//first set him to go home
        }
        else if(runtype%10==0 && runtype > 200){//every 10th turn?
            //breadcrumb
            spit("blue");
            turnHome();
        }
        else if(runtype%10!=0 && runtype > 200){//every turn not / 10?
            turnHome();

        }else if(runtype==200){
            runtype = 256;//reset at 200 to continue top of this loop
        } else if(runtype <200 && runtype >100){
            //a water go around left
            if(atWater())
                reRoute();

            if(runtype%5 ==0){
                //every 5th turn...
                turnHome();
            } else if(runtype==100){
                runtype =199;
            } else{
                bounceLeft();
            }
        } else if(runtype < 100){
            //a water go around right
        }

        if(runtype ==1)
            runtype=255;
            
        setMemory(runtype-1);

    }
    /** continues the route of the greep hopefully towards tomatoes
    * @author Russell Fair
    * @since 0.1
    */    
    public void continueSearch()
    {
        int runtype = getMemory();
        
        if(runtype==255){
            turnHome();//first set him to go away from home
            turn(180);
        }

        else if(runtype%10==0 && runtype > 200){//every 10th turn?
            //breadcrumb
            //spit("blue");
            // turnHome();
        }
        else if(runtype%10!=0 && runtype > 200){//every turn not / 10?
            // turnHome();

        }else if(runtype==200){
            runtype = 256;//reset at 200 to continue top of this loop
        } else if(runtype <200 && runtype >100){
            
        } else if(runtype < 100){

        }

        if(runtype ==1)
            runtype=255;
            
        setMemory(runtype-1);

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
            //spit("blue");
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
        //spit("red");
    }

    /**
    * turn around silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void bounceBack()
    {
        turn(180-Greenfoot.getRandomNumber(90));
        //a near total turn around, but not entirely.
    }


    /**
    * turn right silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void bounceRight()
    {
        turn(10);
        //turn(90-Greenfoot.getRandomNumber(90));
        //a near total turn around, but not entirely.
    }

    /**
    * turn left silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void bounceLeft()
    {
        turn(-10);
        //turn(360-Greenfoot.getRandomNumber(90));
        //a near total turn around, but not entirely.
    }

    /**
    * go away silly greep
    * @author Russell Fair
    * @since 0.2
    */
    public void bounce()
    {
       int rando = Greenfoot.getRandomNumber(360)-Greenfoot.getRandomNumber(360);
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
