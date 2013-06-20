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
        if(hasBouncedBack())
            setBackwards();

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
    *unsets the "is backwards" flag
    * @author Russell Fair
    * @since 0.3
    */
    public void unsetBackwards(){
        setFlag(2, false);
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
                //need to do Deliver
                doDelivery();
            }
            move();//done once while having tomatoes
        }
        else {//not found tomatoes yet
        
            if(!atFoodPile()){
                //need to do Search
                doSearch();
                move();
            }
            else //at food pile...
            {
                //wait? 
                turnAwayShip();
                checkFood();
                resetBrain();
                
                if(pileEmpty()){
                    doSearch();
                    move();
                }
                   
            }
           
        }
    }
    
    /**
    * the new method for searching
    * @author Russell Fair
    * @since 0.5
    */
    public void doSearch(){
        int step = getMemory();

        if(atWorldEdge()){
            setBounced();
            redirect();
        }
        else if(atWater()){
            redirect();
        }
        else if(step > 240){
            //spit("orange");
            //don't do anything for the first few moves
        }
        else if(atShip()){
            //redirect();
            //spit("orange");
            //resetBrain();
        }
        else if(checkBreadcrumb()){
            //if sees breadcrumb
            followBreadcrumb();
        }
        else if(atShip()){

            //prevent greep from getting stuck at ship.
            redirect();
        }
        else if(step%10==0){
            redirect();
        }
        else {
            // checkBreadcrumb();
        }

        //prevent searching greeps from going below 200...
        if(step <= 200)
            step=250;

        loopCounter(step);
    }

    /**
    * the new method for delivering
    * @author Russell Fair
    * @since 0.5
    */
    public void doDelivery(){
        int step = getMemory();

        if(atWorldEdge()){
            setBounced();
            redirect();
            breadcrumb();
            step =195;
        }
        else if(atWater()){
            setBounced();
            breadcrumb();
            redirect();
            step =195;
        }
        else if(step>=200){
            //spit("red");
            turnHome();
            step =195;
        }
        else if(step>=190){
            breadcrumb();
            //do nothing for a few turns...
        }
        else if(checkBreadcrumb()){
            bounceOffBreadcrumb();
        }
        else if(step%10==0){
            //do something special every 10 turns
            turnHome();
           
            
        }
        else{
            //don't turn
            breadcrumb();
        }
        
        loopCounter(step);
    }

    /**
    * leaves the paint trail
    */
    public void breadcrumb(){
        if(carryingTomato()){
            if(atWater()){
                if(isBackwardFacing()){
                    spit("red");
                }else{
                    spit("orange");
                }
            }else{
                spit("purple");
            }
            
        }
            
    }

    /**
    * checks the paint trail
    */
    public boolean checkBreadcrumb(){
        if(seePaint("purple")||seePaint("red")||seePaint("orange"))
           return true;
       else
            return false;
    }

    /**
    * follows the paint trail
    */
    public boolean followBreadcrumb(){
        if(seePaint("purple"))
            turnAwayShip();
        else if(seePaint("red"))
            goRight();
        else if (seePaint("orange"))
            goLeft();

        return false;
    }
    /**
    * stay off other greeps' paint trail
    */
    public void bounceOffBreadcrumb(){
        if(seePaint("red"))
            goLeft();
        else if (seePaint("orange"))
            goRight();

    }


    /**
    * sets the memory to one less than previous
    */
    public void loopCounter(int step){
        if(step==1)
            step=255;

        setMemory(step-1);
    }

    /**
    * goes right or left
    */
    public void redirect(){


        changeDirection(isBackwardFacing());

    }

    /**
    * turn around
    */
    public void turnaround(){
        setRotation(getRotation() - 180);
    }


    /**
    * turn around
    */
    public void turnAwayShip(){
        turnHome();
        turn(180);
    }

    public void changeDirection(boolean neg){

        int angle = (atWorldEdge()) ? 40 : 9;
        
        if(neg)
            setRotation(getRotation() - angle);
        else 
            setRotation(getRotation() + angle);
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
       turn(10);
    }

    /**
    * go around something, to the left
    * @author Russell Fair
    * @since 0.2
    */
    public void goLeft()
    {
       turn(-10);
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
