function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and
%   sigma. You should complete this function to return the optimal C and
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example,
%                   predictions = svmPredict(model, Xval);
%
%  Note: You can compute the prediction error using
%        mean(double(predictions ~= yval))
%

VALS = [0.01, 0.03, 0.1, 0.3, 1, 3, 10, 30];

best_err = 999999999999;

for c_i = 1:length(VALS)
    for sig_i = 1:length(VALS)
        model= svmTrain(X, y, VALS(c_i), @(x1, x2) gaussianKernel(x1, x2, VALS(sig_i)));
        predictions = svmPredict(model, Xval);
        pred_err = mean(double(predictions ~= yval));
        fprintf('C: %.2f, S: %.2f, err: %.2f, min: %.2f', VALS(c_i), VALS(sig_i), pred_err, best_err);
        if pred_err < best_err
            disp('new min!');
            sigma = VALS(sig_i);
            C = VALS(c_i);
            best_err = pred_err;
        end
    end
end

% =========================================================================

end
