public void enhancedSearch(){
	        if(atWater() && atWorldEdge()){
            turnaround();
            setBackwards();
        } else if(atWater()){
            redirect();
        } else if(seePaint("purple")){
            //keep going straight please
            turnAwayShip();
        }   
        else if (seePaint("orange")){
            redirect();
         }
         //else if (seePaint("red")){
        //     //turnAwayShip();
        //     //changeDirection(false);
        // }
        else if(atWorldEdge()&&!hasBouncedBack()){
            setBounced();
            //spit("red");
            redirect();
        } else if(atWorldEdge()&&hasBouncedBack()){
            setBackwards();
            //spit("orange");
            redirect();
        }
        else if(step == 250){
           //nothing do do here
        }else if(step == 225){
           //after 30 turns turn a bit...
            redirect();
        }else if(step == 200){
            step = 255;
           //reset steps to 255 to prevent it from going below 200
        }
        else{
            //continue straight, do nothing.  
        }
}

public void enhancedDelivery(){
	if(atWorldEdge()&&!hasBouncedBack()){
            setBounced();
            redirect();
        }else if(atWorldEdge()&&hasBouncedBack()){
            setBackwards();
            redirect();
        //}else if(seePaint("purple")){
            //keep going straight please, on the trail of another greep
            //turnHome();
        }
        else if(step == 100){
            //redirect();
            spit("orange");
        }
        else if(atWater()){
            breadcrumb();
            redirect();
            setMemory(100);
        }
        else if(step <=100 && step >= 95){
            //redirect();
            //stay straight for 5 turns
            // breadcrumb();
            
        
        }
        
        else if(step%10==0){
            //leave a breadcrumb every 10 turns
            breadcrumb();
            //turnHome();
        }
        
        else{
            //continue straight, do nothing.
            turnHome();  
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

    