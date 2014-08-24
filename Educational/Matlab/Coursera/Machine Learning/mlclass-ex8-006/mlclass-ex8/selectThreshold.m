function [bestEpsilon bestF1] = selectThreshold(yval, pval)
% finds the best threshold to use for selecting outliers based on the
% results from a validation set (pval) and the ground truth (yval).

bestEpsilon = 0;
bestF1 = 0;
F1 = 0;

stepsize = (max(pval) - min(pval)) / 1000;
for epsilon = min(pval):stepsize:max(pval)

  % ====================== YOUR CODE HERE ======================
  % Instructions: Compute the 1 score of choosing epsilon as the
  %               threshold and place the value in F1.
  % You can use predictions = (pval < epsilon) to get a binary vector

  % Note that 1 means anomaly, 0 means normal

  predictions = pval < epsilon;
  tp = sum((predictions == 1) & (yval == 1));
  fp = sum((predictions == 1) & (yval == 0));
  fn = sum((predictions == 0) & (yval == 1));
  prec = tp / (tp + fp);
  rec = tp / (tp + fn);
  F1 = 2 * prec * rec / (prec + rec);

  % =============================================================

  if F1 > bestF1
    bestF1 = F1;
    bestEpsilon = epsilon;
  end
end

end
