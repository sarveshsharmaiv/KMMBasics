//
//  Extensions.swift
//  iosApp
//
//  Created by Sarvesh Sharma on 11/12/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import UIKit

extension UIImageView {
    

    func loadImageUsingUrlString(urlString: String) {
        var imageUrlString: String?
        let imageCache = NSCache<NSString, UIImage>()
        imageUrlString = urlString
        
        guard let url = URL(string: urlString) else { return }
        
        image = nil
        
        if let imageFromCache = imageCache.object(forKey: urlString as NSString) {
            self.image = imageFromCache
            return
        }
        
        URLSession.shared.dataTask(with: url, completionHandler: { (data, respones, error) in
            
            if error != nil {
                print(error ?? "")
                return
            }
            
            DispatchQueue.main.async {
                guard let imageToCache = UIImage(data: data!) else { return }
                
                if imageUrlString == urlString && imageUrlString != "" {
                    self.image = imageToCache
                }
                imageCache.setObject(imageToCache, forKey: urlString as NSString)
            }
            
        }).resume()
    }
}
