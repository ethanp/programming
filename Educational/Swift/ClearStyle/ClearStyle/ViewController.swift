//
//  ViewController.swift
//  ClearStyle
//
//  Created by Ethan Petuchowski on 8/14/15.
//  Copyright (c) 2015 Ethanp. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, TableViewCellDelegate {

    @IBOutlet weak var tableView: UITableView!
    var toDoItems = [ToDoItem]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.registerClass(TableViewCell.self, forCellReuseIdentifier: "cell")
        if toDoItems.count > 0 {
            return
        }
        toDoItems.append(ToDoItem(text: "feed the cat"))
        toDoItems.append(ToDoItem(text: "buy eggs"))
        toDoItems.append(ToDoItem(text: "watch WWDC videos"))
        toDoItems.append(ToDoItem(text: "rule the Web"))
        toDoItems.append(ToDoItem(text: "buy a new iPhone"))
        toDoItems.append(ToDoItem(text: "darn holes in socks"))
        toDoItems.append(ToDoItem(text: "write this tutorial"))
        toDoItems.append(ToDoItem(text: "master Swift"))
        toDoItems.append(ToDoItem(text: "learn to draw"))
        toDoItems.append(ToDoItem(text: "get more exercise"))
        toDoItems.append(ToDoItem(text: "catch up with Mom"))
        toDoItems.append(ToDoItem(text: "get a hair cut"))
        tableView.separatorStyle = .None
        // make table black under the cell you’re dragging
        tableView.backgroundColor = UIColor.blackColor()
        tableView.rowHeight = 50.0
    }
    
    // MARK: - Table view data source
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView,
        numberOfRowsInSection section: Int) -> Int {
        return toDoItems.count
    }
    
    func tableView(tableView: UITableView,
        cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
            let cell = tableView.dequeueReusableCellWithIdentifier(
                "cell", forIndexPath: indexPath) as! TableViewCell
            cell.selectionStyle = .None
            // handled now by StrikeThroughText
//            cell.textLabel?.backgroundColor = UIColor.clearColor()
            let item = toDoItems[indexPath.row]
            // this is now set in TableViewCell on the `didSet` observer for
            // the internal ToDoItem
//            cell.textLabel?.text = item.text
            cell.delegate = self
            cell.toDoItem = item
            return cell
    }
    
    // for pre-iOS 8?
    func tableView(tableView: UITableView,
        heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
            return tableView.rowHeight;
    }
    
    func toDoItemDeleted(toDoItem: ToDoItem) {
        let index = (toDoItems as NSArray).indexOfObject(toDoItem)
        if index == NSNotFound { return }
        
        // could removeAtIndex in the loop but keep it here for when indexOfObject works
        toDoItems.removeAtIndex(index)
        
        // use the UITableView to animate the removal of this row
        tableView.beginUpdates()
        let indexPathForRow = NSIndexPath(forRow: index, inSection: 0)
        tableView.deleteRowsAtIndexPaths([indexPathForRow], withRowAnimation: .Fade)
        tableView.endUpdates()    
    }

    // MARK: - Table view delegate
    
    func colorForIndex(index: Int) -> UIColor {
        let itemCount = toDoItems.count - 1
        let val = (CGFloat(index) / CGFloat(itemCount)) * 0.6
        return UIColor(red: 1.0, green: val, blue: 0.0, alpha: 1.0)
    }
    
    func tableView(tableView: UITableView,
        willDisplayCell cell: UITableViewCell,
        forRowAtIndexPath indexPath: NSIndexPath) {
            cell.backgroundColor = colorForIndex(indexPath.row)
    }
}

