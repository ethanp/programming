//
//  StrikeThroughText.swift
//  ClearStyle
//
//  Created by Ethan Petuchowski on 8/14/15.
//  Copyright (c) 2015 Ethanp. All rights reserved.
//

import UIKit
import QuartzCore

// A UILabel subclass that can optionally have a strikethrough.
class StrikeThroughText: UILabel {
    // strikeThroughLayer is basically a white layer that is re-positioned
    // (by resizeStrikeThrough) according to the size of the rendered text.
    // This layer is hidden if strikeThrough is false, and visible if
    // strikeThrough is true. The resizeStrikeThrough method is called when
    // strikeThrough gets set to true.
    let strikeThroughLayer: CALayer
    // A Boolean value that determines whether the label should have a strikethrough.
    var strikeThrough : Bool {
        didSet {
            strikeThroughLayer.hidden = !strikeThrough
            if strikeThrough {
                resizeStrikeThrough()
            }
        }
    }
    
    required init(coder aDecoder: NSCoder) {
        fatalError("NSCoding not supported")
    }
    
    override init(frame: CGRect) {
        strikeThroughLayer = CALayer()
        strikeThroughLayer.backgroundColor = UIColor.whiteColor().CGColor
        strikeThroughLayer.hidden = true
        strikeThrough = false
        
        super.init(frame: frame)
        layer.addSublayer(strikeThroughLayer)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        resizeStrikeThrough()
    }
    
    let kStrikeOutThickness: CGFloat = 2.0
    func resizeStrikeThrough() {
        let textSize = text!.sizeWithAttributes([NSFontAttributeName:font])
        strikeThroughLayer.frame = CGRect(x: 0, y: bounds.size.height/2,
            width: textSize.width, height: kStrikeOutThickness)
    }
}