/* Assignment 2: Neural Networks
* Description: A program that trains a Neural Network that trains to read handwritten digits 
* Written by Connor Ettinger
* 10/22/24 (modified 7/2/25)
*/
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
public class NeuralNetworkMain{
  private static final double eta = 3;
  private static final int miniBatchSize = 10;
  private static final int numOfEpochs = 30;
  // these two booleans are used in a variety of checks throughout the program
  // the load function takes a bool, if true it loads testing data and if false it loads training data, which is why these vars are assigned like this
  private static final boolean loadTraining = false;
  private static final boolean loadTest = true;
  public static void main(String[] args) throws NullPointerException, FileNotFoundException {
    boolean quit = false; // tells us to quit when true
    Scanner scanIn = new Scanner(System.in);
    // ask the user how to make the network. Other options are restricted until a network exists
    System.out.println("Please choose an option:\n1: Generate and train a new network\n2: Load a pretrained network\n0: Exit the program");
    NeuralNetwork network = null;
    while(scanIn.hasNextLine()){
      String line = scanIn.nextLine();
      // if the user chose option 1, train a new network
      if(line.charAt(0) == '1'){
        System.out.println("\nTraining new network");
        network = trainNewNetwork();
        System.out.println("Network trained");
        break;
        // if the user chose option 2, load a network from neural_network.csv
      } else if (line.charAt(0) == '2'){
        System.out.println("\nPlease input the name of the csv file the network will be loaded from (leave blank for default): ");
        String fileName = scanIn.nextLine();
        if(fileName.trim().isEmpty()){
          fileName = null;
          System.out.println("\nLoading neural network from\"neural_network.csv\"");
        } else{
          System.out.println("\nLoading neural network from \"" + fileName +"\"");
        }
        
        boolean loadSuccess = false;
        try {
          network = NeuralNetwork.loadNeuralNetwork(fileName);
          System.out.println("Network loaded.");
          loadSuccess = true;
        } catch (Exception e) {
          System.err.println("Error: file could not be loaded");
          e.printStackTrace();
        }
        if(loadSuccess){
          break;
        }
        
        // if the user types 0 or q (for quit), quit the program
      } else if (line.charAt(0) == '0' || line.charAt(0) == 'q'){
        quit = true;
        break;
      } else{
        System.out.println("\nError: What you typed was not a valid option.\nPlease type \"1\" to generate and train a new neural network, type \"2\" to load a new network, or type \"0\" to exit the program\n");
      }

      if(quit != true){
        System.out.println("\nPlease choose an option:\n1: Generate and train a new network\n2: Load a pretrained network\n0: Exit the program");
      }
    }

    if(quit){
      System.out.println("\nQuitting program. Thanks for looking at this Neural Network!");
      scanIn.close();
      System.exit(0);
    }

    // now that the network exists, give the user all the options
    System.out.println("\nPlease choose an option:\n1: Generate and train a new network\n2: Load a pretrained network\n3: Save the network state to file\n4: Display network accuracy on TRAINNG data\n5: Display network accuracy on TESTING data\n6: Run network on testing data and show testing images and labels for each piece of data\n7: Display misclassified testing images\n0: Exit the program\n ");
    while(scanIn.hasNextLine()){
      if(quit != true){
        String line = scanIn.nextLine();
        // since there's so many options, I'm using a switch case instead of 8 if-else statements
        switch(line.charAt(0)){
          case '1': // the user wants to train a new network
            System.out.println("\nTraining new network");
            network = trainNewNetwork();
            System.out.println("Network trained");
            break;
          case '2': // the user wants to load the network from neural_network.csv, they can do that
            System.out.println("\nPlease input the name of the csv file the network will be loaded from (leave blank for default): ");
            String fileName = scanIn.nextLine();
            if(fileName.trim().isEmpty()){
              fileName = null;
              System.out.println("\nLoading neural network from\"neural_network.csv\"");
            } else{
              System.out.println("\nLoading neural network from \"" + fileName +"\"");
            }

            try {
              network = NeuralNetwork.loadNeuralNetwork(fileName);
              System.out.println("Network loaded.");
            } catch (Exception e) {
              System.err.println("Error: file could not be loaded");
              e.printStackTrace();
            }

            break;
          case '3':
            System.out.println("\nPlease input the name of the csv file the network will be loaded from (leave blank for default): ");
            String fileName2 = scanIn.nextLine();
            if(fileName2.trim().isEmpty()){
              fileName2 = null;
              System.out.println("\nSaving network to \"neural_network.csv\"");
            } else{
              System.out.println("\nSaving network to \"" + fileName2 +"\"");
            }

            try {
              NeuralNetwork.saveNeuralNetwork(network, fileName2);
              System.out.println("Network saved.");
            } catch (Exception e) {
              System.err.println("Error: network could not be saved");
              e.printStackTrace();
            }

            break;
          case '4': // display the accuarcy of the network on TRAINING data
            System.out.println("\nNetwork accuracy on training data:");
            displayAccuracy(network, loadTraining);
            break;
          case '5': // display the accuarcy of the network on TESTING data
            System.out.println("\nNetwork accuracy on testing data:");
            displayAccuracy(network, loadTest);
            break;
          case '6': // show the ascii images
            System.out.println("\nDisplaying testing images");
            // Run network on testing data showing images and labels data
            displayTestCase(network, false, scanIn);
            break;
          case '7': // show the ascii images for the stuff it got wrong
            System.out.println("\nDisplaying misclassified testing images");
            // Display misclassified testing images
            displayTestCase(network, true, scanIn);
            break;
          case '0': // fallthrough to q
          case 'q': // if the user types q, quit
            quit = true;
            System.out.println("\nQuitting program. Thanks for looking at this Neural Network!");
            scanIn.close();
            System.exit(0);
            break;
          default: // if the user types something that is wrong
            System.out.println("\nError: What you typed was not a valid option.\nPlease type \"1\" to generate and train a new neural network, type \"2\" to load a new network, or type \"0\" to exit the program\n");
        }
        System.out.println("\nPlease choose an option:\n1: Generate and train a new network\n2: Load a pretrained network\n3: Save the network state to file\n4: Display network accuracy on TRAINNG data\n5: Display network accuracy on TESTING data\n6: Run network on testing data and show testing images and labels for each piece of data\n7: Display misclassified testing images\n0: Exit the program\n ");
      } else{ // if quit is true, break out of the while loop
        break;
      }
    }
  }

  public static NeuralNetwork trainNewNetwork() throws NullPointerException, FileNotFoundException {
    // first load training data
    DataSet[] trainingData = loadData(loadTraining);
    // now create the minibatches
    MiniBatch[] miniBatchArr = createMiniBatchs(trainingData);
    // now generate a new NeuralNetwork
    NeuralNetwork defualtNeuralNetwork = NeuralNetwork.generateNewNeuralNetwork();
    // output the accuarcy of this new random network
    System.out.println("New random network generated. Network accuracy before any training:");
    displayAccuracy(defualtNeuralNetwork, loadTraining);
    System.out.println("---------------------------------------------------------------");
    /*************************** begin training ***************************/
    // epoch 1
    NeuralNetwork updatedNeuralNetwork = executeEpoch(defualtNeuralNetwork, miniBatchArr,1);
    System.out.println("Epoch 1 Complete");
    printAccuracy(trainingData);
    System.out.println("---------------------------------------------------------------");
    // loop through the subsquent epochs
    for (int i = 0; i < numOfEpochs-1; i++) {
      MiniBatch[] newMiniBatchArr = createMiniBatchs(trainingData); // randomize minibatches
      updatedNeuralNetwork = executeEpoch(updatedNeuralNetwork, newMiniBatchArr, i+2);
      // now that the Epoch is completed, print the info for it
      System.out.println("Epoch " + (i+2) + " Complete");
      printAccuracy(trainingData);
      System.out.println("---------------------------------------------------------------");
    }
    // once all the epochs are complete, notify the user and return the final neural network
    System.out.println("ALL EPOCHS COMPLETE.");
    return updatedNeuralNetwork;
  }

  private static NeuralNetwork executeEpoch(NeuralNetwork neuralNetwork, MiniBatch[] mbArr, int epochNum){
    System.out.println("Begining Epoch " + epochNum + ":");
    NeuralNetwork updatedNeuralNetwork = null;
    // loop through each minibatch
    for(int i = 0; i < mbArr.length; i ++){
      if(i == 0){
        updatedNeuralNetwork = executeMiniBatch(neuralNetwork, mbArr[i]);
      } else{
        updatedNeuralNetwork = executeMiniBatch(updatedNeuralNetwork, mbArr[i]);
      }
    }
    return updatedNeuralNetwork;
  }

  private static NeuralNetwork executeMiniBatch(NeuralNetwork neuralNetwork, MiniBatch miniBatch){
    NeuralNetwork[] trainingCases = new NeuralNetwork[miniBatchSize]; // this holds the NeuralNetwork with the weight gradient and bias gradient calculated for each layer during each training case
    DataSet[] dataSets = miniBatch.getAllDataSets(); // grab the data for each training case
    // execute all training cases
    for(int i = 0; i < miniBatchSize; i++){
      trainingCases[i] = executeTrainingCase(neuralNetwork, dataSets[i]);
    }

    // now update the weights and biases for the network
    final int hiddenLayerID = 0;
    final int outputLayerID = 1;
    Layer newHiddenLayer = reviseWeightsAndBiases(trainingCases, miniBatch.getSize(),hiddenLayerID);
    Layer newOutputLayer = reviseWeightsAndBiases(trainingCases, miniBatch.getSize(),outputLayerID);
    // create a new neural network with the updated layers, and return that
    NeuralNetwork updatedNeuralNetwork = new NeuralNetwork(newHiddenLayer, newOutputLayer);
    return updatedNeuralNetwork;
  }

  private static NeuralNetwork executeTrainingCase(NeuralNetwork neuralNetwork, DataSet data){
    // unpack each of the three layers
    Layer inputLayer = new Layer(data.getInputs().getRowLen(), data.getInputs());
    Layer hiddenLayer = neuralNetwork.getHiddenLayer().copyLayer();
    Layer outputLayer = neuralNetwork.getOutputLayer().copyLayer();

    // do a forward pass through the layers
    forwardPass(hiddenLayer, inputLayer);
    forwardPass(outputLayer, hiddenLayer);

    // now, before we do back propogation, check how accurate our current data is
    checkAccuracy(outputLayer, data);

    // now, back propogate through each layer
    backPropogation(outputLayer, data.getExpectedOutputs());
    backPropogation(hiddenLayer, outputLayer);

    // create a new neural network with layers that contain the weight and bias gradients, and return it
    NeuralNetwork calcdNeuralNetwork = new NeuralNetwork(hiddenLayer, outputLayer);
    return calcdNeuralNetwork;
  }

  public static void forwardPass(Layer currlayer, Layer prevLayer){
    currlayer.setPreviousActivations(prevLayer); // layers don't have the info on their previous activations by default, because it needs to be calculated or loaded. Now we have that data, so set those activations
    currlayer.calculateActivations();
  }

  public static void checkAccuracy(Layer outputLayer, DataSet data){
    // get the index in our outputs with the biggest number
    int largestIndex = outputLayer.getGuess();
    // now we check if that equals the label, and if it is record that our neural network is accurate for the data used in this testCase
    // otherwise, record that the neural network is wrong
    boolean isCorrect = false;
    if(largestIndex == data.getLabel()){
      isCorrect = true;
    }
    data.setCorrect(isCorrect);
  }

  // back propogation for final layer
  public static void backPropogation(Layer currLayer, Matrix expectedOutput){
    currLayer.calculateBiasGradient(expectedOutput);
    currLayer.calculateWeightGradient();
  }

  // back propogation for non final layers
  public static void backPropogation(Layer currLayer, Layer lPlusOne){
    currLayer.calculateBiasGradient(lPlusOne);
    currLayer.calculateWeightGradient();
  }

  public static Layer reviseWeightsAndBiases(NeuralNetwork[]testCases, int miniBatchSize, int layerID){
    // large method incoming
    final int hiddenLayerID = 0;
    // for reference, outputLayerID = 1

    // first calculate the new weights
    // Wⁿᵉʷ = Wᵒˡᵈ - (η/m) * Σ(∇W)
    Matrix weightGradientSum;
    if(layerID == hiddenLayerID){ // sum the weight gradients for the hidden layer
      weightGradientSum = testCases[0].getHiddenLayer().getWeightGradient().add(testCases[1].getHiddenLayer().getWeightGradient());
      for(int i = 2; i<testCases.length; i++){
        weightGradientSum = weightGradientSum.add(testCases[i].getHiddenLayer().getWeightGradient()); // this one line cost me about 6 hours
      }
    } else { // sum the weight gradients for the output layer
      weightGradientSum = testCases[0].getOutputLayer().getWeightGradient().add(testCases[1].getOutputLayer().getWeightGradient());
      for(int i = 2; i<testCases.length; i++){
        weightGradientSum = weightGradientSum.add(testCases[i].getOutputLayer().getWeightGradient());
      }
    }

    // next, calculate η/m
    double etaOverM = eta / miniBatchSize;
    // (η/m) * Σ(∇W)
    Matrix multEtaByWeightGradSum = weightGradientSum.multByConst(etaOverM);
    // Wⁿᵉʷ = Wᵒˡᵈ - (η/m) * Σ(∇W)
    Matrix newWeights;
    if(layerID == hiddenLayerID){ // calc new weights for hidden layer
      newWeights = testCases[0].getHiddenLayer().getWeights().subtact(multEtaByWeightGradSum);
    } else { // calc new weights for output layer
      newWeights = testCases[0].getOutputLayer().getWeights().subtact(multEtaByWeightGradSum);
    }

    // next calculate the new biases
    // Bⁿᵉʷ = Bᵒˡᵈ - (η/m) * Σ(∇B)
    Matrix biasGradientSum;
    if(layerID == hiddenLayerID){ // sum the bias gradients for the hidden layer
      biasGradientSum = testCases[0].getHiddenLayer().getBiasGradient().add(testCases[1].getHiddenLayer().getBiasGradient());
      for(int i = 2; i<testCases.length; i++){
        biasGradientSum = biasGradientSum.add(testCases[i].getHiddenLayer().getBiasGradient());
      }
    } else { // sum the bias gradients for the output layer
      biasGradientSum = testCases[0].getOutputLayer().getBiasGradient().add(testCases[1].getOutputLayer().getBiasGradient());
      for(int i = 2; i<testCases.length; i++){
        biasGradientSum = biasGradientSum.add(testCases[i].getOutputLayer().getBiasGradient());
      }
    }
    // we already calcd η/m earlier so we don't need to do it again
    // (η/m) * Σ(∇B) 
    Matrix multEtaByBiasGradSum = biasGradientSum.multByConst(etaOverM);
    // Bⁿᵉʷ = Bᵒˡᵈ - (η/m) * Σ(∇B)
    Matrix newBiases;
    if(layerID == hiddenLayerID){
      newBiases = testCases[0].getHiddenLayer().getBiases().subtact(multEtaByBiasGradSum);
    } else {
      newBiases = testCases[0].getOutputLayer().getBiases().subtact(multEtaByBiasGradSum);
    }

    // now create a new layer with our new weights and biases, and return that
    Layer revisedLayer;
    if(layerID == hiddenLayerID){ // create new hidden layer
      revisedLayer = new Layer(testCases[0].getHiddenLayer().getSize(), newWeights.getFullMatrix(), newBiases.getFullMatrix());
    } else { // create new output layer
      revisedLayer = new Layer(testCases[0].getOutputLayer().getSize(), newWeights.getFullMatrix(), newBiases.getFullMatrix());
    }
    return revisedLayer;
  }

  public static DataSet[] loadData(boolean whatToLoad) throws NullPointerException, FileNotFoundException{
    // thanks to https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html and https://www.geeksforgeeks.org/scanner-class-in-java/ for help with the scanner class
    DataSet[] dataArr; // the data set array
    Scanner scanCSV; // the scanner used to read the csv
    // create the scanner and number of datasets we have for testing and training data respectively
    if(whatToLoad == loadTest){
      scanCSV = new Scanner(new File("mnist_test.csv"));
      dataArr = new DataSet[10000];
    } else {
      scanCSV = new Scanner(new File("mnist_train.csv"));
      dataArr = new DataSet[60000];
    }

    // go over every line in the csv
    int csvLineNum = 0;
    while(scanCSV.hasNextLine()){
      // grab the line, turn it into an array of 785 numbers
      String line = scanCSV.nextLine();
      String[] values = line.split(",");
      // the fist number in each line is the label
      // so grab that and turn it into a hot vector that we can use
      int label = Integer.valueOf(values[0]);
      Matrix hotVector = new Matrix(10, 1); // recall our hotVector is a 10x1 matrix
      // recall matricies are initialized to being full of 1s (go check the Matrix class for more info),
      // so iterate over the matrix and change every value to 0 EXECPT for the label (leaving it as 1)
      for(int i = 0; i < 10; i++){
        if(i != label)
          hotVector.setIndex(i, 0, 0);
      }

      // turn the remaining 784 values into a 784x1 matrix
      Matrix inputMatrix = new Matrix(784, 1);
      for(int j = 1; j <= 784; j++){
        // scale the value from 0-255 to 0-1
        // i forgot to do this at first. this took my network from an accuarcy of about 20% to 95+%
        double defaultValue = Double.valueOf(values[j]);
        double scalledValue = defaultValue/255.0;

        inputMatrix.setIndex(j-1, 0, scalledValue);
      }

      // now, create a new dataset with our values
      // set the setNum for later, so we can see which cases we get right vs wrong
      DataSet data = new DataSet(csvLineNum,inputMatrix, hotVector);
      // add that dataset to the array
      dataArr[csvLineNum] = data;

      // increment the line num by 1
      csvLineNum++;
    }
    // now we have all of the data in an array, so close our scanner and return that array
    scanCSV.close();
    return dataArr;
  }

  public static MiniBatch[] createMiniBatchs(DataSet[] trainingData){
    // figure out how many minibatches go in an epoch
    int numOfMiniBatches = trainingData.length / miniBatchSize;
    // create an array to store all of the minibatches
    MiniBatch[] miniBatchArr = new MiniBatch[numOfMiniBatches];

    // randomize the order of the training data
    // thanks to Aiden Plauche for pointing me in the direction of Collections.shuffle, and to https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array for helping me implement it
    Collections.shuffle(Arrays.asList(trainingData));

    // now create each minibatch, assign it data from the randomized dataset, and add it to the miniBatch array
    int arrIndex = 0;
    for (int i = 0; i < numOfMiniBatches; i++) {
      // create the array of datasets that the minibatch will have
      DataSet[] mbDataSets = new DataSet[miniBatchSize];
      // populate the array
      for(int j = 0; j < mbDataSets.length; j++){
        mbDataSets[j] = trainingData[arrIndex];
        arrIndex++;
      }
      // create a new minibatch
      MiniBatch newMinibatch = new MiniBatch(i, miniBatchSize, mbDataSets);
      // add it to the minibatch array
      miniBatchArr[i] = newMinibatch;
    }
    return miniBatchArr;
  }

  // can display the accuarcy for a network on both the training and testing data
  public static void displayAccuracy(NeuralNetwork network, boolean whatToLoad) throws NullPointerException, FileNotFoundException{
    DataSet[] data = loadData(whatToLoad); // load our data set
    // for each piece of data, have the network do a forward pass through that data,then check if the network got it right
    for(int i = 0; i < data.length; i++){
      // forward pass thru the data
      Layer inputLayer = new Layer(data[i].getInputs().getRowLen(), data[i].getInputs());
      Layer hiddenLayer = network.getHiddenLayer().copyLayer();
      Layer outputLayer = network.getOutputLayer().copyLayer();
      forwardPass(hiddenLayer, inputLayer);
      forwardPass(outputLayer, hiddenLayer);
      // check if we got it right
      checkAccuracy(outputLayer, data[i]);
    }
    // now that we know which pieces of data the network got right and wrong, print that data
    printAccuracy(data);
  }

  public static void printAccuracy(DataSet[] dataSetArr){
    // an AccuarcyData object holds info about how accurate the netowork was on recognizing a specific digit
    // create 10 of them, 1 for each digit 0-9, the get the stats on our network's accuarcy
    AccuracyData[] numDataArr = new AccuracyData[10];
    numDataArr = getAccuracyStats(dataSetArr);

    // next, for each of the digits, output how many there were and how many the network got correct
    // also, keep track of the total amount there were and the total amount the network got correct
    int totalAmount = 0;
    int totalAmountCorrect = 0;
    for(int i = 0; i < 10; i++){
      System.out.println("Digit " + i + ": " + numDataArr[i].getAmountOfCorrect() + "/" + numDataArr[i].getAmount());
      totalAmount += numDataArr[i].getAmount();
      totalAmountCorrect += numDataArr[i].getAmountOfCorrect();
    }
    // next, calculate what % of the data overall the network got correct
    double percentCorrect = ((double)totalAmountCorrect / (double)totalAmount) * 100.0;
    String percentCorrectStr = String.format("%.3f", percentCorrect);
    // finally, print the overall accuarcy stats
    System.out.println("\nOverall Accuracy: " + totalAmountCorrect + "/" + totalAmount +" = " + percentCorrectStr +"%");
  }

  public static AccuracyData[] getAccuracyStats(DataSet[] dataSetArr){
    // initalize the info holders for each of the 10 digits
    AccuracyData[] accDataArr = new AccuracyData[10];
    for(int i = 0; i < 10; i++){
      AccuracyData accData = new AccuracyData(i);
      accDataArr[i] = accData;
    }
    // for each of our datasets in the dataset array
    for(DataSet dataSet : dataSetArr) {
      // first grab the label
      int digit = dataSet.getLabel();
      // next, increment the amount of whatever digit it is
      accDataArr[digit].incrementAmount();
      // check if the data was labeled correctly
      if(dataSet.isCorrect()){
        // if it's labeled correctly as well, increment the amount of times the data was labeled correctly for this number
        accDataArr[digit].incrementAmountOfCorrect();
      }
      // reset if the data was labeled correctly
      dataSet.setCorrect(false);
    }
    return accDataArr;
  }

  // this is the function that displays the ascii image for each test case, along with the info on if we got it right
  // this function can also be set to only display incorrect values
  public static void displayTestCase(NeuralNetwork network, boolean onlyIncorrect, Scanner sc) throws NullPointerException, FileNotFoundException{
    DataSet[] data = loadData(loadTest); // load in our data
    // randomize the order of the data so that we don't always see it in the same order every time
    // thanks again to Aiden Plauche for pointing me in the direction of Collections.shuffle, and to https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array for helping me implement it
    Collections.shuffle(Arrays.asList(data));
    // now, for each point of data: do a forward pass through it, check if we got it right
    // then, display the info and print the ascii representation of the data
    // then weight for user input before continuing
    for(int i = 0; i < data.length; i++){
      // forward pass thru the data
      Layer inputLayer = new Layer(data[i].getInputs().getRowLen(), data[i].getInputs());
      Layer hiddenLayer = network.getHiddenLayer().copyLayer();
      Layer outputLayer = network.getOutputLayer().copyLayer();
      forwardPass(hiddenLayer, inputLayer);
      forwardPass(outputLayer, hiddenLayer);
      // check if it's right
      checkAccuracy(outputLayer, data[i]);
      // this if statment makes sure we skip ones we got right when we only want to display incorrect options
      if(!onlyIncorrect || (onlyIncorrect && !data[i].isCorrect())){
        // create the info about the image and the classificaiton and stuff
        String s = "Testing Case #" + data[i].getSetNum();
        s+= ":\tCorrect Classification = " + data[i].getLabel();
        s+= "\tNetwork Output = " + outputLayer.getGuess();
          if(data[i].isCorrect()){
            s+= "\tCorrect.";
          } else{
            s+= "\tIncorrect.";
          }
        System.out.println(s);
        // print the test case
        printTestCaseImage(data[i].getInputs());
        // instruct the user how to continue, wait for input
        System.out.println("Enter \"1\" to continue, anything else to return to the main menu.");
        String userInput = sc.next();
        // only continue if the user pressed one, otherwise break early
        if(userInput.charAt(0) != '1'){
          break;
        }
      } else {
        continue;
      }
    }
  }

  public static void printTestCaseImage(Matrix inputData){ // this prints the image as a bunch of ascii chars
    final int sideSize = 28; // it's a 28x28 image
    int arrIndex = 0; // where we are in the 784x1 matrix
    String s = ""; // the string that will be printed
    for(int i = 0; i < sideSize; i++){
      for(int j = 0; j < sideSize; j++){
        // get the value of the current pixel we're on
        double currValScaled = inputData.getIndex(arrIndex, 0) * 255.0; // recall that we divide the value by 255 to scale it to a value between 0-1. so we multiply it by 255 to scale it back to its original value
        int currVal = (int)currValScaled;
        // calculate what range the pixel falls into, and add that char representaion to the string
        if(currVal <= 25){ // 0-25
          s+=" ";
        } else if(currVal > 25 && currVal <= 50){ // 25-50
          s+=".";
        } else if(currVal > 50 && currVal <= 75){ // 50-75
          s+="'";
        } else if(currVal > 75 && currVal <= 100){ // you get the idea
          s+="*";
        } else if(currVal > 100 && currVal <= 125){
          s+="i";
        } else if(currVal > 125 && currVal <= 150){
          s+="c";
        } else if(currVal > 150 && currVal <= 175){
          s+="X";
        } else if(currVal > 175 && currVal <= 200){
          s+="N";
        } else if(currVal > 200 && currVal <= 225){
          s+="&";
        } else{ // if currVal > 225, the 225-255 range
          // yes, I'm aware that this range is technically 5 spots bigger than the other ranges, but it really isn't worth making a whole other range just for 5 chars
          s+="#";
        }
        arrIndex++; // move to the next pixel
      }
      s+="\n"; // at the end of the line, add a line break to move to the next line
    }
    System.out.println(s); // print our pretty picture <3
  }
}

class MiniBatch {
  /* a minibatch has an ID (so we know which one it is), a size, 
   * and a list of dataSets (one dataSet for each test case) */
  private int ID;
  private int size;
  private DataSet[] dataSets;
  public MiniBatch(int mbNum, int mbSize, DataSet[] data){
    this.ID = mbNum;
    this.size = mbSize;
    this.dataSets = data;
  }

  public int getSize(){
    return this.size;
  }

  public DataSet[] getAllDataSets(){
    return this.dataSets;
  }

}

class DataSet {
  /* data has a Marix of inputs (X), and a Matrix of expectedOutputs (Y),
   * a setNum so we know which set we're on,
   * a label which tells us what the expected output of the dataset is (but not as a matrix),
   * and a boolean that tells us if the set was labeled correctly by the NeuralNetwork */
  private int setNum;
  private Matrix inputs;
  private Matrix outputs;
  private int label;
  private boolean identifiedCorrectly;
  public DataSet(int num, Matrix in, Matrix out){
    this.setNum = num;
    this.inputs = new Matrix(in);
    this.outputs = new Matrix(out);
    // determine the label
    for(int i = 0; i < outputs.getRowLen(); i++){
      if(outputs.getIndex(i, 0) == 1)
        this.label = i;
    }
  }

  // some getters
  public int getSetNum(){
    return this.setNum;
  }

  public Matrix getInputs(){
    return this.inputs;
  }

  public Matrix getExpectedOutputs(){
    return this.outputs;
  }

  public int getLabel(){
    return this.label;
  }

  public boolean isCorrect(){
    return this.identifiedCorrectly;
  }

  // only one setter because this is the only one that is used in the code, and this file is already bloated as is
  public void setCorrect(boolean isCorrect){
    this.identifiedCorrectly = isCorrect;
  }
}

class NeuralNetwork {
  /* a neural network has two instance variables: one for the hidden layer and one for the output layer.
   * NOTE: the input layer isn't on there becuase it's just not worth storing
   * it also has three static variables: one for the size of each layer
   * (and yes we are storing the input here because it's important. don't worry about it) */
  private Layer hiddenLayer;
  private Layer outputLayer;
  private static final int inputSize = 784;
  private static final int hiddenSize = 15;
  private static final int outputSize = 10;
  public NeuralNetwork(Layer l1, Layer l2){
    this.hiddenLayer = l1;
    this.outputLayer = l2;
  }

  public Layer getHiddenLayer(){
    return this.hiddenLayer;
  }

  public Layer getOutputLayer(){
    return this.outputLayer;
  }

  public static NeuralNetwork generateNewNeuralNetwork(){
    // generate random weights and biases for the hidden layer
    double[][] hiddenRandWeights = generateRandMatrix(NeuralNetwork.hiddenSize, NeuralNetwork.inputSize);
    double[][] hiddenRandBiases = generateRandMatrix(NeuralNetwork.hiddenSize, 1);
    // create the hidden layer and assign it to this neural network
    Layer hidden = new Layer(NeuralNetwork.hiddenSize, hiddenRandWeights, hiddenRandBiases);

    // generate random weights and biases for the output layer
    double[][] outputRandWeights = generateRandMatrix(NeuralNetwork.outputSize, NeuralNetwork.hiddenSize);
    double[][] outputRandBiases = generateRandMatrix(NeuralNetwork.outputSize, 1);
    // create the output layer and assign it to this neural network
    Layer output = new Layer(NeuralNetwork.outputSize, outputRandWeights, outputRandBiases);
    
    NeuralNetwork nn = new NeuralNetwork(hidden, output);
    return nn;
  }

  private static double[][] generateRandMatrix(int numOfRows, int numOfCols){
    double[][] randMatrix = new double[numOfRows][numOfCols];
    for(int i = 0; i < numOfRows; i++){
      for(int j = 0; j < numOfCols; j++){
        // generates a random double between 0-1
        double randNum = Math.random();
        // 50-50 chance to make the random double negative, so that we can generate numbers between -1 and 1
        if(Math.random() < 0.5){
          randNum = randNum * -1.0;
        }
        randMatrix[i][j] = randNum;
      }
    }
    return randMatrix;
  }

  public static NeuralNetwork loadNeuralNetwork(String fileName) throws NullPointerException, FileNotFoundException {
    // this method is so ugly, with so much repeated code, but it works so we're moving on
    // set the defualt file name is none are provided
    if(fileName == null){
      fileName = "neural_network.csv";
    }
    else if(!fileName.endsWith(".csv")){
      fileName += ".csv";
    }
    Scanner reader = new Scanner(new File(fileName));

    // read the weights for the hidden layer in
    String hwLine = reader.nextLine();
    String[] hwValues = hwLine.split(",");

    // create and populate the hidden weights matrix
    double[][] hiddenWeights = new double[NeuralNetwork.hiddenSize][NeuralNetwork.inputSize];
    int arrIndex = 0;
      for(int i = 0; i < NeuralNetwork.hiddenSize; i++){
        for(int j = 0; j < NeuralNetwork.inputSize; j++){
          hiddenWeights[i][j] = Double.valueOf(hwValues[arrIndex]);
          arrIndex++;
        }
      }

    // read the biases for the hidden layer in
    String hbLine = reader.nextLine();
    String[] hbValues = hbLine.split(",");

    // create and populate the hidden biases matrix
    double[][] hiddenBiases = new double[NeuralNetwork.hiddenSize][1];
    for(int i = 0; i < NeuralNetwork.hiddenSize; i++){
      hiddenBiases[i][0] = Double.valueOf(hbValues[i]);
    }

    // create the hidden layer
    Layer hidden = new Layer(NeuralNetwork.hiddenSize, hiddenWeights, hiddenBiases);

    // read the weights for the output layer in
    String outWLine = reader.nextLine();
    String[] outWValues = outWLine.split(",");

    // create and populate the output weights matrix
    double[][] outputWeights = new double[NeuralNetwork.outputSize][NeuralNetwork.hiddenSize];
    arrIndex = 0; // reset arrIndex
      for(int i = 0; i < NeuralNetwork.outputSize; i++){
        for(int j = 0; j < NeuralNetwork.hiddenSize; j++){
          outputWeights[i][j] = Double.valueOf(outWValues[arrIndex]);
          arrIndex++;
        }
      }

    // read the biases for the output layer in
    String outBLine = reader.nextLine();
    String[] outBValues = outBLine.split(",");

    // create and populate the output biases matrix
    double[][] outputBiases = new double[NeuralNetwork.outputSize][1];
    for(int i = 0; i < outputSize; i++){
      outputBiases[i][0] = Double.valueOf(outBValues[i]);
    }

    // create the output layer
    Layer output = new Layer(NeuralNetwork.outputSize, outputWeights, outputBiases);

    reader.close();
    NeuralNetwork nn = new NeuralNetwork(hidden, output);
    return nn;
  }

  public static void saveNeuralNetwork(NeuralNetwork network, String fileName){
    // set the defualt file name is none are provided
    if(fileName == null){
      fileName = "neural_network.csv";
    } else if(!fileName.endsWith(".csv")){
      fileName += ".csv";
    }
    String hiddenWeights = network.getHiddenLayer().getWeights().toCSVFormat();
    String hiddenBiases = network.getHiddenLayer().getBiases().toCSVFormat();
    String outputWeights = network.getOutputLayer().getWeights().toCSVFormat();
    String outputBiases = network.getOutputLayer().getBiases().toCSVFormat();
    try {
      // first, over write whatever was in the file before we accessed it
      FileWriter overWriter = new FileWriter(fileName, false);
      overWriter.write(hiddenWeights);
      overWriter.close();
      // next, append the rest of our info to the file
      FileWriter appender = new FileWriter(fileName, true);
      appender.write(hiddenBiases);
      appender.write(outputWeights);
      appender.write(outputBiases);
      appender.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}

class Layer {
  /* a layer has a size, which is the number of nodes in that layer,
   * a weight matrix, a bias matrix, an activation matrix, an activation matrix from the previous layer,
   * an error matrix, a biaseGradient matrix, and a weightGradient matrix */
  private final int size; // final because you can't change the size of a layer.
  private Matrix weights;
  private Matrix biases;
  private Matrix activations;
  private Matrix prevActivations;
  private Matrix error;
  private Matrix biasGradient;
  private Matrix weightGradient;

  // constructor for layer
  public Layer(int layerSize, double[][] weight, double[][] bias){
  if (layerSize > 0)
      this.size = layerSize;
    else
      this.size = 1;
    this.weights = new Matrix(weight);
    this.biases = new Matrix(bias);
    this.prevActivations = null;
    this.activations = new Matrix(this.size, 1);
    this.weightGradient = new Matrix(this.weights.getRowLen(), this.weights.getColLen());
    this.biasGradient = new Matrix(this.biases.getRowLen(), this.biases.getColLen());
    this.error = this.biasGradient;
  }

  // constructor for if there is no previous layer (aka for the input layer)
  public Layer(int s, Matrix layer0){
    if (s > 0)
      this.size = s;
    else
      this.size = 1;
    this.weights = null;
    this.biases = null;
    this.prevActivations = new Matrix(layer0);
    this.activations = new Matrix(this.size, 1);
    this.activations.populateMatrix(layer0);
    this.weightGradient = null;
    this.biasGradient = null;
  }

  // getters and setters
  // getters
  public int getSize(){
    return this.size;
  }

  public Matrix getWeights(){
    return this.weights;
  }

  public Matrix getBiases(){
    return this.biases;
  }

  public Matrix getActivations(){
    return this.activations;
  }

  public Matrix getWeightGradient(){
    return this.weightGradient;
  }

  public Matrix getBiasGradient(){
    return this.biasGradient;
  }

  public Matrix getError(){
    return this.error;
  }

  // this returns the index with the largest value, aka what our layer is guessing
  // only really used for the output layer
  public int getGuess(){
    int largestIndex = 0;
    double largestValue = this.activations.getIndex(0, 0);
    for(int i = 0; i < this.activations.getRowLen(); i++) {
      if(largestValue < this.activations.getIndex(i, 0)){
        largestIndex = i;
        largestValue = this.activations.getIndex(i, 0);
      }
    }
    return largestIndex;
  }

  // setters (setPreviousActivations is the only one used in the code, so it's the only one included)

  public void setPreviousActivations(Matrix m){
    this.prevActivations = m;
  }

  public void setPreviousActivations(Layer l){
    this.prevActivations = l.getActivations();
  }

  // Class Methods
  public void calculateActivations(){
    // A = σ( W • A^L-1 + B)
    // get W • A^L-1
    Matrix dotProdMatrix = this.weights.dotProduct(this.prevActivations);
    // now add B
    Matrix zMatrix = dotProdMatrix.add(this.biases);
    // now apply the sigmoid function to get the activation
    this.activations = this.applySigmoidFunction(zMatrix);
  }

  // calculate error in final layer
  public void calculateError(Matrix y){
    // (Aᴸ - Y) * Aᴸ * (1-Aᴸ)
    // NOTE: "*"" here means "hadamard product"

    // get (Aᴸ - Y)
    Matrix aMinusY = this.activations.subtact(y);
    // do the rest of the equation
    calcErrorHelper(aMinusY);
  }

  // calculate error in a non-final layer
  public void calculateError(Layer lPlusOne){
    //((W^l+1)ᵀ • 𝛿 ^(l+1) * Aᴸ * (1-Aᴸ)
    // NOTE: "*"" here means "hadamard product"

    // get (W^l+1)ᵀ
    Matrix transposedWeights = lPlusOne.getWeights().transpose();
    // dot product that with 𝛿 ^l+1
    Matrix firstPart = transposedWeights.dotProduct(lPlusOne.getError());
    // do the rest of the equation
    calcErrorHelper(firstPart);
  }

  private void calcErrorHelper(Matrix firstPart){
    /* since both error equations are 
        (first part) * Aᴸ * (1-Aᴸ)
      where "*" = "hadamard product", we can just calculate the first part in two methods,
      then send that first part to this helper method to avoid repeating code
    */
    Matrix one = new Matrix(this.activations.getRowLen(), this.activations.getColLen()); // recall a new matrix is populated with ones
    // 1-Aᴸ
    Matrix oneMinusA = one.subtact(this.activations);
    // (first part) * Aᴸ * (1-Aᴸ)
    this.error = firstPart.hadamardProduct(this.activations.hadamardProduct(oneMinusA));

  }

  // calculate bias gradient for final layer
  public void calculateBiasGradient(Matrix y){
    // ∇Bˡ = 𝛿ˡ
    this.calculateError(y);
    this.biasGradient = this.error;
  }

  // calculate bias gradient for non-final layer
  public void calculateBiasGradient(Layer lPlusOne){
    // ∇Bˡ = 𝛿ˡ
    this.calculateError(lPlusOne);
    this.biasGradient = this.error;
  }

  public void calculateWeightGradient(){
    // ∇Wˡ = ∇Bˡ • (A^l-1)ᵀ
    this.weightGradient = this.biasGradient.dotProduct(this.prevActivations.transpose());
  }

  public Matrix applySigmoidFunction(Matrix m){
    Matrix sigAppliedMatrix = new Matrix(m.getRowLen(), m.getColLen());
    // apply the sigmoid function to each value in m
    for(int i = 0; i < sigAppliedMatrix.getRowLen(); i++){
      for(int j = 0; j < sigAppliedMatrix.getColLen(); j++){
        // σ(z) = 1/(1+e^(-z))
        double result = 1/(1 + Math.exp(-1 * m.getIndex(i, j)));
        // add the new value to the matrix we're returning
        sigAppliedMatrix.setIndex(i, j, result);
      }
    }
    return sigAppliedMatrix;
  }

  // method for creating a deep copy of a layer
  public Layer copyLayer(){
    Matrix copiedWeights = new Matrix(this.weights);
    Matrix copiedBiases = new Matrix(this.biases);
    Layer copiedLayer = new Layer(this.size, copiedWeights.getFullMatrix(), copiedBiases.getFullMatrix());
    return copiedLayer;
  }
}

class Matrix {
  /* a matrix has a column length, which is how many columns the matrix has, NOT the length of each column
   * a row length, which is how many rows the matrix has, NOT the length of each row
   * a 2D array for the actual matrix, and a boolean to tell us if it's empty or not 
   * 
   * At the end of this project, like 30+ hours later, I really regret the way I labeled the row and col lenghth variables. 
   * If I were to do this again, I'd give them a differnet name */
  private int colLen;
  private int rowLen;
  private double[][] matrix;

  // constructor
  public Matrix(int rows, int cols){
    this.colLen = cols;
    this.rowLen = rows;
    this.matrix = new double[this.rowLen][this.colLen];
    for (int i = 0; i < rowLen; i++){
      for(int j = 0; j < colLen; j++){
        // the reason we set this to one is so if we wanna create a constant matrix later, we can just multiply that constant by a new matrix.
        // and, if we want the 0 matrix, we can just multiply it by 0
        this.matrix[i][j] = 1;
      }
    }
  }

  // constructor for if you have another matrix
  public Matrix(Matrix m){
    this.colLen = m.getColLen();
    this.rowLen = m.getRowLen();
    this.matrix = new double[this.rowLen][this.colLen];
    this.populateMatrix(m);
  }

  // constructor for if you have a 2D array
  public Matrix(double[][] m){
    this.colLen = m[0].length;
    this.rowLen = m.length;
    this.matrix = new double[this.rowLen][this.colLen];
    this.populateMatrix(m);
  }

  // getters and setters
  // getters

  public int getColLen(){
    return this.colLen;
  }

  public int getRowLen(){
    return this.rowLen;
  }

  public double[][] getFullMatrix(){
    return this.matrix;
  }

  // getter for a specific index in the matrix
  public double getIndex(int row, int col){
    return this.matrix[row][col];
  }

  // getter for a specific row in the matrix
  public double[] getRow(int row){
    return this.matrix[row];
  }

  // getter for a specific column in the matrix, returns the column transposed
  public double[] getCol(int col){
    double[] returnCol = new double[this.rowLen];
    for(int i = 0; i < this.rowLen; i++){
      returnCol[i] = this.matrix[i][col];
    }
    return returnCol;
  }

  // note: there is no setRowLen() or setColLen() as that would require changing the lengths of the rows and columns of the whole matrix, which is too much work. if you wanna do that, make a new matrix with the right lens and copy the old one into it

  // note: there is also no setFullMatrix() setter for the whole matrix. if you wanna do that, either use populateMatrix() or set the rows and columns by hand


  // setters
  // setter for a specific index in the matrix
  public void setIndex(int row, int col, double val){
    this.matrix[row][col] = val;
  }

  // setter for a specific row in the matrix
  public void setRow (int row, double[] val){
    if (val.length == this.colLen){
      this.matrix[row] = val;
    } else{
      System.err.println("The row you are trying to set is not the same length as the rows of this matrix. Please try again");
      System.err.println("The row length for the matrix is: " + this.rowLen + ", the length of the row you are trying to set is: " + val.length);
    }
  }

  // setter for a specific column in the matrix. takes an array and transposes it onto the matrix
  public void setCol (int col, double[] val){
    if (val.length == this.colLen){
      for (int i = 0; i < this.colLen; i++)
        this.matrix[i][col] = val[i];
    }
  }

  // class methods
  // populate the current matrix with another matrix
  public void populateMatrix(Matrix m){
    // check and make sure the matricies are the same size
    if(this.colLen == m.getColLen() && this.rowLen == m.getRowLen()){
      // if so, loop through and copy the index at each position in m to this
      for(int i = 0; i < this.rowLen; i++){
        for(int j = 0; j < this.colLen; j++){
          this.matrix[i][j] = m.getIndex(i,j);
        }
      }
    } else{
      System.err.println("The two matrices are not the same size. Please make sure they are the same size, then try again.");
      System.err.println("The left matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the right matrix is a " + m.getRowLen() + " x " + m.getColLen() + " matrix.");
    }
  }

  // populate the current matrix with a 2D array of the same size
  public void populateMatrix(double[][] m){
    // this whole first part of the method is to check and make sure the 2D array is the same size as the matrix
    boolean sameSize = true;
    // first check and make sure the rows are the same size
    if (this.rowLen != m.length){
      sameSize = false;
    } else{ // now check each column and make sure they are all the correct size
      for (int i = 0; i < this.rowLen; i++){
        if (this.colLen != m[i].length){
          sameSize = false;
          break;
        }
      }
    }
    // if the 2D array is the same size as our matrix, then we can copy the values from m into our matrix
    if(sameSize){
      for(int i = 0; i < this.rowLen; i++){
        for(int j = 0; j < this.colLen; j++){
          this.matrix[i][j] = m[i][j];
        }
      }
    } else{
      System.err.println("The 2D array is not the same size as the matrix. Please make sure they are the same size, then try again.");
      System.err.println("The matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the 2D array is a " + m.length + " x " + m[0].length + " array.");
    }
  }

  // return a new matrix that is the transpose of the current one
  public Matrix transpose(){
    // create a new matrix with the rowLen and colLen swapped
    Matrix temp = new Matrix(this.colLen, this.rowLen);
    // set each row in the new matrix to the column in this matrix
    for(int i = 0; i < this.colLen; i++){
      temp.setRow(i, this.getCol(i));
    }
    return temp; // return the new matrix
  }

  // return a new matrix that is the dot product of this matrix and B
  public Matrix dotProduct(Matrix B){
    // A(mxn) * B(nxp) = AB(mxp)
    // make sure the matricies can actually be multiplied
    if(this.colLen == B.getRowLen()){
      Matrix prodMatrix = new Matrix(this.rowLen, B.getColLen());
      // dot product each row in A (this) by each column in B
      for(int i = 0; i < B.getColLen(); i++){
        for(int j = 0; j < this.rowLen; j++){
          double[] row = this.matrix[j];
          double[] col = B.getCol(i); // note that getCol transposes the column before returning it as a 1D array, so we now have two arrays of the same length
          
          double prod = 0;
          // for each index in the row and column, multiply them together, then add them to the current dot product
          for (int k = 0; k < row.length; k++) {
            prod += row[k]*col[k];
          }
          // add the dot product of row A and col B to the product matrix
          prodMatrix.setIndex(j, i, prod);
        }
      }
      return prodMatrix;
    } else{
      // if they can't, write an error message and return B
      System.err.println("The two matrices are not the correct size to apply the dot product funtion. Please make sure they are the correct sizes, then try again.");
      System.err.println("The left matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the right matrix is a " + B.getRowLen() + " x " + B.getColLen() + " matrix.\nHowever, in order to do a dot product, the number of columns in the left matrix (" + this.colLen + ") must be equal to the number of rows in the right matrix (" + B.getRowLen() +"). " + this.colLen + " != " + B.getRowLen() +".");
      return B;
    }
  }

  // return a new matrix that is the hadamard product of this matrix and B
  public Matrix hadamardProduct(Matrix B){
    // make sure the matricies are the same size so they can be Hadamarded™
    if (this.rowLen == B.getRowLen() && this.colLen == B.getColLen()){
      Matrix prodMatrix = new Matrix(this.rowLen, this.colLen);
      for(int i = 0; i < this.rowLen; i++){
        for(int j = 0; j < this.colLen; j++){
          double prod = this.getIndex(i, j) * B.getIndex(i, j);
          prodMatrix.setIndex(i, j, prod);
        }
      }
      return prodMatrix;
    } else{
      System.err.println("The two matrices are not the same size. Please make sure they are the same size, then try again.");
      System.err.println("The left matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the right matrix is a " + B.getRowLen() + " x " + B.getColLen() + " matrix.");
      return B;
    }
  }

    // return this matrix multiplied by a constant as a new matrix
    public Matrix multByConst(double constant){
      Matrix prodMatrix = new Matrix(this.rowLen, this.colLen);
      for(int i = 0; i < this.rowLen; i++){
        for(int j = 0; j < this.colLen; j++){
          double prod = constant * this.getIndex(i, j);
          prodMatrix.setIndex(i, j, prod);
        }
      }
      return prodMatrix;
    }

  // return the sum of this matrix and another matrix m as a new matrix
  public Matrix add(Matrix m){
    if(this.rowLen == m.getRowLen() && this.colLen == m.getColLen()){
      Matrix sumMatrix = new Matrix(this.rowLen, this.colLen);
      for (int i = 0; i < this.rowLen; i++) {
        for (int j = 0; j < this.colLen; j++) {
          double sumIndex = (this.matrix[i][j] + m.getIndex(i, j));
          sumMatrix.setIndex(i, j, sumIndex);
        }
      }
      return sumMatrix;
    } else{
      System.err.println("The two matrices are not the same size. Please make sure they are the same size, then try again.");
      System.err.println("The left matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the right matrix is a " + m.getRowLen() + " x " + m.getColLen() + " matrix.");
      return(m);
    }
  }

  // return the difference of this matrix and another matrix m as a new matrix
  public Matrix subtact(Matrix m){
    if(this.rowLen == m.getRowLen() && this.colLen == m.getColLen()){
      return(this.add(m.multByConst(-1.0)));
    }
    else{
      System.err.println("The two matrices are not the same size. Please make sure they are the same size, then try again.");
      System.err.println("The left matrix is a " + this.rowLen + " x " + this.colLen + " matrix, while the right matrix is a " + m.getRowLen() + " x " + m.getColLen() + " matrix.");
      return(m);
    }
  }

  // turn the matrix into a string
  // could be better but it works so we're moving on
  public String toString(){
    String s = "";
    for(int i = 0; i < this.rowLen; i++){
      for(int j = 0; j < this.colLen; j++){
        s += String.format("%.5f ", this.matrix[i][j]);
      }
      s += "\n";
    }
    return s;
  }

  // turn the matrix into a string that's ready to be written to a csv, which is every number in the matrix followed by a comma
  public String toCSVFormat(){
    String s = "";
    for(int i = 0; i < this.rowLen; i++){
      for(int j = 0; j < this.colLen; j++){
        s += this.matrix[i][j] + ",";
      }
    }

    // because our for loop puts a comma behing every number, there is a comma behind the last number in the array. we need to remove that last comma
    // thanks to https://www.baeldung.com/java-remove-last-character-of-string for reminding me how to remove last character of a string
    s = s.substring(0, s.length()-1);
    // finally, add a new line to end of the string
    s += "\r\n";
    return s;
  }
}

class AccuracyData {
  private int number; // this is which number we're looking at, from 0-9
  private int numberAmount; // this is how many times the number appeared in the training data
  private int numberCorrectAmount; // this is how many times the neural network got the number correct
  public AccuracyData(int num){
    this.number = num;
    this.numberAmount = 0;
    this.numberCorrectAmount = 0;
  }

  public int getAmount(){
    return this.numberAmount;
  }

  public int getAmountOfCorrect(){
    return this.numberCorrectAmount;
  }

  public void incrementAmount(){
    this.numberAmount++;
  }

  public void incrementAmountOfCorrect(){
    this.numberCorrectAmount++;
  }
}