package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //////main menu//////
    //music dialog
    Dialog music;
    Intent serviceIntent;
    boolean isPlay; // if music is played
    Button btnPlay; // start/stop music

    //battery dialog
    Dialog battery;
    ImageView imgBattery; // battery image
    BroadCastBattery broadCastBattery;


    //////login/register screen stuff//////
    ImageButton imgBtnLoginRegister; // btn login or register


    //////login/register dialog//////
    Dialog login;
    Button bSubmitLoginRegister; // submit button in the login/register dialog
    EditText etUssername,etPassword; // edit text of username and password


    //////menu screen stuff//////
    ImageButton imgBtnNewGame,imgBtnHowToPlay; // images buttons in the menu of how to play and new game


    //////how to play dialog//////
    Dialog howToPlay;
    Button bClose; // btn close in the how to play dialog


    //////winner dialog//////
    Dialog winner;
    Drawable[] imgWinneres = new Drawable[2]; // array images of you loose and u win
    ImageView imgWinner; // image view of the winner
    Button btnBackToMenu; // btn to go back to menu from the winner dialog


    //////game stuff//////
    Snakesandladders snakesnladders; // object with all the snakes and ladder locations
    int rolled_number; // what number was rolled
    Context context;
    Button btnRoll; // btn to roll the cube
    int winner_play;//who is winner? 0 is player, 1 is bot
    int turnPlay;//whose turn?
    int outcome; // where is the position u need to go if u hit a ladder or a snake


    //////board stuff//////
    final int maxN=10;//declare int for board size
    Pointer[] pointer = new Pointer[2];//player 0, bot 1
    Drawable[] drawCell = new Drawable[4];//3 is empty,0 is player,1 is bot,2 is background

    //////dice dialog//////
    TextView tvRolled; // represent the number the cube last rolled


    //declare for imageView (Cells) array
    ImageView [][] ivCell =  new ImageView[maxN][maxN]; // the board




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgBtnLoginRegister = findViewById(R.id.login_image_btn);
        imgBtnLoginRegister.setOnClickListener(this);
    }


    // menu control
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // if an item was selected witch one
        for(int i=0;i<menu.size();i++)
        {
            MenuItem item= menu.getItem(i);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return  true;
    }
    // if the menu was clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_music) {
            Toast.makeText(this,"you selected music",Toast.LENGTH_LONG).show();
            createMusic();
            return true;
        }
        else if (id== R.id.action_battery) {
            Toast.makeText(this, "you selected battery", Toast.LENGTH_LONG).show();
            createBattery();
            return true;
        }
        return true;
    }

    //music stuff
    private void createMusic() { // create music dialog
        music = new Dialog(this);
        music.setContentView(R.layout.music_dialog);
        music.setTitle("Music");
        music.setCancelable(true);
        isPlay=false;
        btnPlay= music.findViewById(R.id.btnStart);
        btnPlay.setOnClickListener(this);
        serviceIntent=new Intent(this, PlayService.class);
        music.show();
    }
    private void buttonPlayClick() {
        if(!isPlay){
            isPlay=true;
            btnPlay.setText("stop");
            playAudio();
        }
        else{
            isPlay=false;
            btnPlay.setText("play");
            stopAudio();
        }
    }

    private void playAudio() {
        try{
            startService(serviceIntent);
        }
        catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void stopAudio() {
        try{
            stopService(serviceIntent);
        }
        catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    //battery stuff
    private void createBattery() { // create battery dialog
        battery = new Dialog(this);
        battery.setContentView(R.layout.battery_dialog);
        battery.setTitle("Battery");
        battery.setCancelable(true);
        imgBattery=battery.findViewById(R.id.imgBattery);
        broadCastBattery=new BroadCastBattery();
        battery.show();
    }
    // get battery information
    private class BroadCastBattery extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery = intent.getIntExtra("level",0);
            Toast.makeText(context,String.valueOf(battery), Toast.LENGTH_SHORT).show();
            if(battery>95){
                imgBattery.setImageResource(R.drawable.green_battery);
            }
            else if(battery<95 && battery>20){
                imgBattery.setImageResource(R.drawable.yellow_battery);
            }
            else{
                imgBattery.setImageResource(R.drawable.red_battery);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadCastBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCastBattery);
    }



    //create login/register dialog
    public void createLoginRegister(){
        login = new Dialog(this);
        login.setContentView(R.layout.login_dialog);
        login.setTitle("Login/Register");
        login.setCancelable(true);
        etUssername = login.findViewById(R.id.etUsername);
        etPassword = login.findViewById(R.id.etPassword);
        bSubmitLoginRegister = login.findViewById(R.id.bSubmit);
        bSubmitLoginRegister.setOnClickListener(this);
        login.show();
    }



    //create client
    private void createClient(String username, String password) {
        User user = new User(username, password); // set up a user
        Client client = new Client(user); // set up a client
        client.start(); // starts the client
        while(!client.isFinished()){
            Log.d("isFinished status: " , String.valueOf(client.isFinished()));
        }
        Log.d("finished" , client.getResponse());
        if(client.getResponse().equals("new user have been created")){ // if its a new user
            menu();
        }
        if(client.getResponse().equals("password match")){ // if the user exited and the password match
            menu();
        }
        if(client.getResponse().equals("password doesnt match")){ // if the username does not exist

        }
    }




    public void menu(){ // responsible on menu features
        setContentView(R.layout.menu);
        imgBtnNewGame = findViewById(R.id.new_game_image_btn);
        imgBtnHowToPlay = findViewById(R.id.how_to_play_image_btn);
        imgBtnNewGame.setOnClickListener(this);
        imgBtnHowToPlay.setOnClickListener(this);
    }


    public void createHowToPlay(){ // create dialog for how to play
        howToPlay = new Dialog(this);
        howToPlay.setContentView(R.layout.how_to_play_dialog);
        howToPlay.setTitle("How to play");
        howToPlay.setCancelable(true);
        bClose = howToPlay.findViewById(R.id.btnClose);
        bClose.setOnClickListener(this);
        howToPlay.show();
    }
    
    
    
    //game functions starts here
    //game run
    private void game(){
        setContentView(R.layout.game);
        context=this;
        loadResources();
        designBoardGame();
        init_game();
        setListen();
        play_game();
    }

    private void play_game() { // starts the game
        // restarts pointers
        for(int d=0;d<pointer.length;d++){
            Pointer p = new Pointer();
            pointer[d] = p;
        }

        // generate snakes and ladders randomly
        snakesnladders = new Snakesandladders();

        //define who goes first
        Random r = new Random();
        turnPlay = r.nextInt(2);//returns value 0 or 1

        if(turnPlay==0){//player play first
            Toast.makeText(context,"Player play first", Toast.LENGTH_SHORT).show();
            playerTurn();
        }
        else {//bot goes first
            Toast.makeText(context,"Bot play first", Toast.LENGTH_SHORT).show();
            botTurn();
        }
    }

    private void playerTurn() { // the player turn
        turnPlay = 0;
        //the game wait until the player will roll the cube
    }

    private void botTurn() { // bot turn
        turnPlay = 1;
        //if this is first move bot the cube will automatically roll a number and show it on the screen
        rolled_number = new Random().nextInt(6) + 1;
        make_a_move();
    }


    private void make_a_move() { // makes the move and swap the turn
        String rolled_number_str = Integer.toString(rolled_number);
        rolled_number_str = "Cube rolled : " + rolled_number_str;
        tvRolled.setText(rolled_number_str);
        make_move_on_board();
        // who is going next
        if(turnPlay==0){
            botTurn();
        }
        else if(turnPlay==1){
            playerTurn();
        }
    }

    private void make_move_on_board(){ // define where to move to and make it on the board
        ivCell[pointer[turnPlay].getX()][pointer[turnPlay].getY()].setImageDrawable(drawCell[2]);


        // if the bot and the player where at the same square and now its the bot turn it will put the player in that square after it emptied the place
        if(turnPlay==1 && pointer[0].getY()==pointer[1].getY() && pointer[0].getX()==pointer[1].getX()){
            ivCell[pointer[0].getX()][pointer[0].getY()].setImageDrawable(drawCell[0]);
        }


        // check where is the new place of the player/bot
        if (pointer[turnPlay].getX()%2==0){ // if the line is even
            if((pointer[turnPlay].getY()-rolled_number)<0) { // if the lane is finished
                pointer[turnPlay].setX(pointer[turnPlay].getX() + 1);
                pointer[turnPlay].setY(-1-(pointer[turnPlay].getY() - rolled_number));
            }
            else{ // if the line is not finished
                pointer[turnPlay].setY(pointer[turnPlay].getY()-rolled_number);
            }
        }
        else{ // if the line is odd
            if((pointer[turnPlay].getY()+rolled_number)>9) { // if the lane is finished
                if (pointer[turnPlay].getX() == 9) {
                    Toast.makeText(context, "Cant go over 100", Toast.LENGTH_SHORT).show();
                }
                else {
                    pointer[turnPlay].setX(pointer[turnPlay].getX() + 1);
                    pointer[turnPlay].setY(19 - (pointer[turnPlay].getY() + rolled_number));
                }
            }
            else if(pointer[turnPlay].getX() == 9 && (pointer[turnPlay].getY()+rolled_number)==9){ // if the player reaches the end
                String winnerStr = Integer.toString(turnPlay);
                winner_play = turnPlay;
                Log.d("winner" , "winner is: " + winnerStr);
                menu();
                createWinner();
            }
            else{ // if the line is not finished
                pointer[turnPlay].setY(pointer[turnPlay].getY()+rolled_number);
            }
        }


        //check if that square have a snake or ladder on it

        if (pointer[turnPlay].getX()%2==0) { // if the line is even
            if(snakesnladders.getSnakenladders().get(pointer[turnPlay].getX()*10 + (9-pointer[turnPlay].getY()))!=null){ // check if there is a snake/ladder in that square u reached
                outcome = snakesnladders.getSnakenladders().get(pointer[turnPlay].getX()*10 + (9-pointer[turnPlay].getY())); // calculate the where to go
                Log.d("ladder/snake: ", "reached ladder/snake go to" + Integer.toString((outcome+1)));

                if(outcome>pointer[turnPlay].getX()*10 + (9-pointer[turnPlay].getY())){ // if its a snake
                    Toast.makeText(context, "reached a ladder going to: " + Integer.toString((outcome+1)), Toast.LENGTH_SHORT).show();
                }
                else{ // if its a ladder
                    Toast.makeText(context, "reached a snake going to: " + Integer.toString((outcome+1)), Toast.LENGTH_SHORT).show();
                }

                if(outcome/10%2==0){ // check if the dozen number is even
                    pointer[turnPlay].setX(outcome/10);
                    pointer[turnPlay].setY(9-outcome%10);
                }
                else{ // check if the dozen number is odd
                    pointer[turnPlay].setX(outcome/10);
                    pointer[turnPlay].setY(outcome%10);
                }
            }
            else{ // if its not a special square with ladder/snake
                Log.d("ladder/snake: ", "not in this square");
            }
        }
        else { // if the line is odd
            if(snakesnladders.getSnakenladders().get(pointer[turnPlay].getX()*10 + (pointer[turnPlay].getY()))!=null){ // check if there is a snake/ladder in that square u reached
                outcome = snakesnladders.getSnakenladders().get(pointer[turnPlay].getX()*10 + (pointer[turnPlay].getY())); // calculate the where to go
                Log.d("ladder/snake: ", "reached ladder/snake go to" + Integer.toString((outcome+1)));

                if(outcome>pointer[turnPlay].getX()*10 + (9-pointer[turnPlay].getY())){ // if its a snake
                    Toast.makeText(context, "reached a ladder going to: " + Integer.toString((outcome+1)), Toast.LENGTH_SHORT).show();
                }
                else{ // if its a ladder
                    Toast.makeText(context, "reached a snake going to: " + Integer.toString((outcome+1)), Toast.LENGTH_SHORT).show();
                }

                if(outcome/10%2==0){ // check if the dozen number is even
                    pointer[turnPlay].setX(outcome/10);
                    pointer[turnPlay].setY(9-outcome%10);
                }
                else{ // check if the dozen number is odd
                    pointer[turnPlay].setX(outcome/10);
                    pointer[turnPlay].setY(outcome%10);
                }
            }
            else{ // if its not a special square with ladder/snake
                Log.d("ladder/snake: ", "not in this square");
            }
        }

        // add the move on the board
        String pointer_x = Integer.toString(pointer[turnPlay].getX());
        String pointer_y = Integer.toString(pointer[turnPlay].getY());
        if(turnPlay==0){
            Log.d("player X: ", pointer_x);
            Log.d("player Y: ", pointer_y);
        }
        else{
            Log.d("bot X: ", pointer_x);
            Log.d("bot Y: ", pointer_y);
        }
        ivCell[pointer[turnPlay].getX()][pointer[turnPlay].getY()].setImageDrawable(drawCell[turnPlay]);
    }

    private void init_game() { //this void will create UI before game start
        winner_play = 0;//none
        for(int i=0;i<maxN;i++){
            for (int j=0;j<maxN;j++){
                ivCell[i][j].setImageDrawable(drawCell[2]);//default or empty cell
                //valueCell[i][j]=0;
            }
        }
    }

    //listener
    public void setListen(){
        btnRoll = findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(this);
        tvRolled = findViewById(R.id.tvRolled);
    }


    public void loadResources(){
        //copy images of the winners
        imgWinneres[0] = context.getResources().getDrawable(R.drawable.you_win_picture);
        imgWinneres[1] = context.getResources().getDrawable(R.drawable.you_lose_picture1);
        drawCell[2] = context.getResources().getDrawable(R.drawable.cell_bg);
        //copy images for player and bot;
        drawCell[3] = null; //empty cell
        drawCell[0] = context.getResources().getDrawable(R.drawable.blue_character_image);//drawable for player
        drawCell[1] = context.getResources().getDrawable(R.drawable.red_character_image);//drawable for bot

    }


    public void designBoardGame() {
        //create layout params to optimize cell size
        //we create horizontal linear layout  for a row
        //which contains maxN imageView in
        int sizeofCell = Math.round(ScreenWidth()/maxN);
        Log.d("Cell zie: ", Integer.toString(sizeofCell));
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCell*maxN, sizeofCell+sizeofCell/4+sizeofCell/25);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCell,sizeofCell+sizeofCell/4+sizeofCell/25);

        LinearLayout linBoardGame = findViewById(R.id.linBoardGame);

        for(int i=0;i<maxN;i++){
            LinearLayout linRow = new LinearLayout(context);
            //make a row
            for (int j=0;j<maxN;j++){
                ivCell[i][j] = new ImageView(context);
                //make a cell
                //need to set background default for cell
                //cell has j status, empty(default,player,bot)
                ivCell[i][j].setImageDrawable(drawCell[2]);
                linRow.addView(ivCell[i][j],lpCell);
            }
            linBoardGame.addView(linRow,lpRow);
        }
    }


    private float ScreenWidth() { // get screen width information
        Resources resources = context.getResources();
        DisplayMetrics dn= resources.getDisplayMetrics();
        return dn.widthPixels;
    }


    public void createWinner(){ // create winner dialog
        winner = new Dialog(this);
        winner.setContentView(R.layout.winner_dialog);
        winner.setTitle("Winner");
        winner.setCancelable(true);
        imgWinner = winner.findViewById(R.id.imgWinner);
        imgWinner.setImageDrawable((imgWinneres[winner_play]));
        btnBackToMenu = winner.findViewById(R.id.btnBackToMenu);
        btnBackToMenu.setOnClickListener(this);
        winner.show();
    }


    @Override
    public void onClick(View v) {
        //////main menu//////
        if(v==btnPlay){
            buttonPlayClick();
        }


        //////login//////
        if(v==imgBtnLoginRegister){
            createLoginRegister();
        }

        if(v==bSubmitLoginRegister){ //if submit is clicked
            /// sends the username and password entered by the user to the server
            login.dismiss();
            createClient(etUssername.getText().toString(), etPassword.getText().toString());
        }


        //////menu//////
        if(v==imgBtnNewGame){
            game();
        }
        if(v==imgBtnHowToPlay){
            createHowToPlay();
        }


        //////how to play//////
        if(v==bClose){
            howToPlay.dismiss();
        }

        //////game//////
        // roll
        if(v==btnRoll){
            rolled_number = new Random().nextInt(6) + 1;
            make_a_move();
        }
        // create winner if there is a winner
        if(v==btnBackToMenu){
            winner.dismiss();
        }
    }


}

