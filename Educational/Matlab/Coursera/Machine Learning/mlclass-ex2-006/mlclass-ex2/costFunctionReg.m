function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta

% old cost code
for i = 1:m
  J += -y(i)*log(sigmoid(theta'*X(i,:)'))-(1-y(i))*log(1-sigmoid(theta'*X(i,:)'));
end
J /= m;

% add regularization [avoiding theta(1)]
for j = 2:size(theta)
  J += lambda * theta(j)^2 / (2*m);
end

% old gradient code
for j = 1:size(grad)
  for i = 1:m
    grad(j) += (sigmoid(theta'*X(i,:)')-y(i))*X(i,j);
  end
  grad(j) /= m;
end

% add regularization
for j = 2:size(grad)
  grad(j) += lambda * theta(j) / m;
end

% =============================================================

end
