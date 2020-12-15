//
//  TemplateListTableViewCell.swift
//  iosApp
//
//  Created by Sarvesh Sharma on 04/12/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import UIKit

class TemplateListTableViewCell: UITableViewCell {
    
    @IBOutlet weak var templateImgView: UIImageView!
    var imageResourceURLString = String()
    var videoResouceURLString = String()
    private lazy var mediaManager: MediaManager? = MediaManager(withView: self.contentView)
    
    override func awakeFromNib() {
        super.awakeFromNib()
        let singleTap = UITapGestureRecognizer(target: self, action: #selector(tapDetected))
        templateImgView.isUserInteractionEnabled = true
        templateImgView.addGestureRecognizer(singleTap)
    }

    @objc func tapDetected() {
        self.mediaManager?.playVideo(withURL: URL(string: videoResouceURLString)!, andKey: "")
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
}

