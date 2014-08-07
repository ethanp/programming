function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the
%   cost of using theta as the parameter for linear regression to fit the
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.
%

% pad the ones (actually this was done on the way in)
% pX = [ones(size(X)) X];

% worked this baby out on paper
J = sum((X*theta-y).^2) / (2*m);

% regularize
J += (lambda / (2*m)) * sum(theta(2:end).^2);

% unregularized (this took a little while)
% the default sum computes over each COL
grad = sum((X*theta-y).*X) / m;

% regularize
reg = theta' * lambda / m;
reg(1) = 0;
grad += reg;

% =========================================================================

grad = grad(:);

end
