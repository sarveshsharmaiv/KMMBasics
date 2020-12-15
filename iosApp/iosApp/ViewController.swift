//
//  ViewController.swift
//  iosApp
//
//  Created by Sarvesh Sharma on 10/12/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var templatesTableView: UITableView!
    private lazy var mediaManager: MediaManager? = MediaManager(withView: self.view)
    var sdk: TemplatesSDK!
    var templates = [Template]()
    override func viewDidLoad() {
        super.viewDidLoad()
        templatesTableView.register(UINib.init(nibName: "TemplateListTableViewCell", bundle: Bundle.main), forCellReuseIdentifier: "TemplateListCell")
        templatesTableView.delegate = self
        templatesTableView.dataSource = self
        getTemplates()
    }
    
    func getTemplates() {
        self.sdk.getTemplates(forceReload: false) {
            (templates, error) in
            if let templates = templates {
                self.templates = templates
                DispatchQueue.main.async {
                    self.templatesTableView.reloadData()
                }
            }
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return templates.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TemplateListCell", for: indexPath) as! TemplateListTableViewCell
        let imageResourceURLString = self.templates[indexPath.row].previewList.story["default"]?.websiteThumbnailURL ?? ""
        cell.imageResourceURLString = imageResourceURLString
        cell.templateImgView.loadImageUsingUrlString(urlString: imageResourceURLString)
        let videoResourceURLString = self.templates[indexPath.row].previewList.story["default"]?.previewURL ?? ""
        cell.videoResouceURLString = videoResourceURLString
        return cell
    }
}

