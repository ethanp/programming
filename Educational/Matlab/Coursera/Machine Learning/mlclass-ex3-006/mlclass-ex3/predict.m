function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)

% Useful values
m = size(X, 1);
num_labels = size(Theta2, 1);

% You need to return the following variables correctly 
p = zeros(size(X, 1), 1);

% ====================== YOUR CODE HERE ======================
% Instructions: Set p to a vector containing labels.
% Hint: max(A, [], 2) to obtain the max for each row.
%

% Add ones to the X data matrix
X = [ones(m, 1) X];

% matrix of Activations
% row = Example
% col = Neuron in layer
a2 = sigmoid(X*Theta1');
a2 = [ones(m, 1) a2];
a3 = sigmoid(a2*Theta2');

[blah, p] = max(a3, [], 2);

% =========================================================================


end
