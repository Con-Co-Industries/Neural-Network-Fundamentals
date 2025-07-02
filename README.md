# Neural-Network-Fundamentals
This was an assignment for my school where I implemented and trained a multi-layer fully connected, feed forward network consisting of sigmoidal neurons, trained with stochastic gradient descent and back propagation. Notably, with the exception of java.util and java.io, no external libraries were used when creating this program.

## How to use
This is a command line application. When starting the program, the user has three options:

0. Quit
1. Train a network
2. Load a pre-trained network

Once a network is present in, the user has 8 options in total:
0. Quit
1. Generate and train a new network
2. Load a pre-trained network
3. Save the network to a file
4. Display network accuracy on training data
5. Display network accuracy on testing data
6. Run network on testing data and show testing images and labels for each piece of data
7. Display misclassified testing images

### Quitting
To quit the program, press "0" or "q" when asked to select an option. Alternatively, just ctrl + c (or cmnd + c on mac)

### Training a network
Press 1 to train a new network.

Once this option is selected, a new network is generated with weights and biases randomly generated as values from -1 to 1.
The program will then begin training the network, using the provided training csv. As it's training, the program will print out information relating to each epoch. Once the training is complete, you will be returned to the options screen.

Some notes on the training:
- The code trains for 30 epochs
- The minibatch size is 10
- The learning rate (referred to as "eta" in the actual code) is 3
- All three of these values can be easily changed by simply changing the variable in main
- The code regularly produces networks that are over 95% accurate

### Loading a pre-trained network
Press 2 to load a pre-trained network

Once you press 2, you will be asked for the name of the file. The program only takes .csv files, so you don't need to put the extension at the end of the name. If you leave it it blank, it will attempt to load the defualt file "neural_network.csv". If the file can't be found, you will get a message telling you why, and then be returned to the options screen.

**NOTE: The program is only able to load files in the same directory as the main file, if you try to specify a different directory it will fail**

### Saving the network to a file
**NOTE: You need to have either trained a network or loaded a network for this option to be avaliable**

Press 3 to save the current network state to a .csv file.