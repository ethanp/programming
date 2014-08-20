function Z = projectData(X, U, K)
%PROJECTDATA Computes the reduced data representation when projecting only
%on to the top k eigenvectors
%   Z = projectData(X, U, K) computes the projection of
%   the normalized inputs X into the reduced dimensional space spanned by
%   the first K columns of U. It returns the projected examples in Z.
%

% You need to return the following variables correctly.
Z = zeros(size(X, 1), K);

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the projection of the data using only the top K
%               eigenvectors in U (first K columns).
%               For the i-th example X(i,:), the projection on to the k-th
%               eigenvector is given as follows:
%                    x = X(i, :)';
%                    projection_k = x' * U(:, k);

% X -- dataset
% U -- principle components
% K -- desired number of of dimensions

% This is because the projection of the first row of X
% would be p_1 = x_1*U(:,1:K)
% and I want /all/ of the projections,
% so that's what the matrix dot-prod gives me
Z = X*U(:,1:K);

% =============================================================

end
