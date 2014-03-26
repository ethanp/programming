Turn view into image to be saved to Camera Roll

    UIGraphicsBeginImageContext(view.bounds.size);
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

Save image code to Camera Roll

    UIImage *i = ...;
    ALAssetsLibrary *library = [[ALAssetsLibrary alloc] init];
    CGImageRef r = i.CGImage;
    [library writeImageToSavedPhotosAlbum:r metadata:nil completionBlock:nil];

