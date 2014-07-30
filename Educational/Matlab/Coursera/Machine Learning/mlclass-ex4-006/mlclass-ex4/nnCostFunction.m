function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices.
%
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network

% I was totally flummoxed by the whole a:b * c thing,
% but it is simply read as a:(b*c)
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);

% You need to return the following variables correctly
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% Part 1: Feedforward
a1 = [ones(m,1) X];
z2 = a1 * Theta1';
a2 = sigmoid(z2);
a2 = [ones(m,1) a2];
a3 = sigmoid(a2*Theta2');

% Create correct_output matrix
% correct_output = zeros(size(y), num_labels);
% for i = 1:size(y)
%     correct_output(i, y(i)) = 1;
% end

% Snazzier way to do it
% 1. Create a matrix containing each of the logical vectors,
% 2. Then create a matrix by mashing those logical vectors we want, together
correct_output = eye(num_labels)(y,:);

% Compute non-regularized Cost "J"
% this works, though it's not vectorized...
for i = 1:m
    for k = 1:num_labels
        J += (-correct_output(i,k)*log(a3(i,k))-(1-correct_output(i,k))*log(1-a3(i,k)));
    end
end
J /= m;

% Regularize Cost "J"
reg = 0;
t1s = Theta1.^2;
st1s = sum(t1s(:,2:end)(:)); % 2:end to chop off bias neuron

t2s = Theta2.^2;
st2s = sum(t2s(:,2:end)(:));
J += (st1s+st2s)*lambda/(2*m);

% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients

% Step 2: delta^(3)
% the matrix of errors associated with each OUTPUT node on each example
d3 = a3 - correct_output;

% Step 3: delta^(2)
% the matrix of errors associated with each HIDDEN node on each example

% size(Theta2'*d3') = [26 5000], so looks like we have
%   Row: hidden neuron
%   Col: training example
%   Val: error associated with each hidden node in corresponding example
% So now, I want to *multiply each error value* by (what I understand to be)
%       how much it would be affected by a miniscule change in weight-value
%   This is because part of the gradient decent algorithm uses the gradient
%       to decide /how far/ to jump in a given direction, though IMHO it
%       would still be "gradient decending" if it used a different means to
%       determine how far to jump.
% size(d2) => 5000 X 25
d2 = d3*Theta2(:,2:end).*sigmoidGradient(z2);

% Step 4: accumulate gradient into Delta^(l)
% In English:
%   * D2 is associated with each weight
%   * It is calculated by summing over each example, how far off was
%     that output neuron, multiplied by how lit up was the hidden neuron
%   * So if it was too low (on average), it will be negative
%   * This will dictate how we alter Theta2 for the next go-round
%   * Nicely, this summation over each example is accomplished via the
%     normal matrix dot-product
D2 = d3'*a2;
D1 = d2'*a1;

% Turn the sum of the errors over the examples (as discussed above)
% into the average error over the examples
% This is our new gradient
Theta2_grad += D2/m;
Theta1_grad += D1/m;

% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%



% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
