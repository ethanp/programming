function [J, grad] = cofiCostFunc(params, Y, R, num_users, num_movies, ...
                                  num_features, lambda)
%COFICOSTFUNC Collaborative filtering cost function
%   [J, grad] = COFICOSTFUNC(params, Y, R, num_users, num_movies, ...
%   num_features, lambda) returns the cost and gradient for the
%   collaborative filtering problem.
%

% Unfold the U and W matrices from params
X = reshape(params(1:num_movies*num_features), num_movies, num_features);
Theta = reshape(params(num_movies*num_features+1:end), ...
                num_users, num_features);


% You need to return the following values correctly
J = 0;
X_grad = zeros(size(X));
Theta_grad = zeros(size(Theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost function and gradient for collaborative
%               filtering. Concretely, you should first implement the cost
%               function (without regularization) and make sure it is
%               matches our costs. After that, you should implement the
%               gradient and use the checkCostFunction routine to check
%               that the gradient is correct. Finally, you should implement
%               regularization.
%
% Notes: X - num_movies  x num_features matrix of movie features
%        Theta - num_users  x num_features matrix of user features
%        Y - num_movies x num_users matrix of user ratings of movies
%        R - num_movies x num_users matrix, where R(i, j) = 1 if the
%            i-th movie was rated by the j-th user
%
% You should set the following variables correctly:
%
%        X_grad - num_movies x num_features matrix, containing the
%                 partial derivatives w.r.t. to each element of X
%        Theta_grad - num_users x num_features matrix, containing the
%                     partial derivatives w.r.t. to each element of Theta
%

% J -- the cost function
comb = X*Theta'.*R;
diffs = comb - Y.*R;
J = sum(diffs(:).^2) / 2;

% regularization of J
first_reg = lambda * sum(sum(Theta.^2)) / 2;
second_reg = lambda * sum(sum(X.^2)) / 2;
J = J + first_reg + second_reg;

% X_grad & Theta_grad (naive)
% for i = 1:num_movies
%   for j = 1:num_users
%     if R(i,j) == 1
%       amt = Theta(j,:)*X(i,:)' - Y(i,j);
%       for k = 1:num_features
%         X_grad(i,k) += amt*Theta(j,k);
%         Theta_grad(j,k) += amt*X(i,k);
%       end
%     end
%   end
% end

% (not-so-naive)
% look at what this has become!!
X_grad = diffs*Theta;
Theta_grad = diffs'*X;


%% Regularize the gradients

% for i = 1:num_movies
%   X_grad(i,:) += lambda*X(i,:);
% end
% for j = 1:num_users
%   Theta_grad(j,:) += lambda*Theta(j,:);
% end

X_grad += lambda*X;
Theta_grad += lambda*Theta;

% =============================================================

grad = [X_grad(:); Theta_grad(:)];

end
