//
//  File.swift
//  iosApp
//
//  Created by Sarvesh Sharma on 07/12/20.
//  Copyright © 2020 orgName. All rights reserved.
//
import UIKit
import AVFoundation
import Cache

class MediaManager: CachingPlayerItemDelegate {
    private var player: AVPlayer?
    private var playerLayer: AVPlayerLayer!
    weak var view: UIView!
    let storage = try? Storage<String, Data>(
      diskConfig: DiskConfig(name: "DiskCache"),
      memoryConfig: MemoryConfig(expiry: .never, countLimit: 10, totalCostLimit: 10),
        transformer: TransformerFactory.forCodable(ofType: Data.self)
    )
    
    init(withView view: UIView) {
        self.view = view
    }
    
    func playVideo(withURL url: URL, andKey key: String) {
        storage?.async.entry(forKey: url.absoluteString, completion: {
            (result) in
            let playerItem: CachingPlayerItem
            switch result {
            case .error(_):
                playerItem = CachingPlayerItem(url: url)
            case .value(let entry):
                playerItem = CachingPlayerItem(data: entry.object, mimeType: "video/mp4", fileExtension: "mp4")
            }
            DispatchQueue.main.async {
                playerItem.delegate = self
                self.player = AVPlayer(playerItem: playerItem)
                self.player?.volume = 1.0
                self.player?.automaticallyWaitsToMinimizeStalling = false
                self.playerLayer = AVPlayerLayer(player: self.player)
                self.playerLayer.frame = self.view.frame
                self.view.layer.addSublayer(self.playerLayer)
                self.player?.play()
            }
        })
    }
    
    
//    self.player = AVPlayer(url: url)
//    self.playerLayer = AVPlayerLayer(player: self.player)
//    self.playerLayer.frame = self.view.frame
//    self.view.layer.addSublayer(self.playerLayer)
//    self.player?.play()
//    func displayImage(withURL url: URL, andKey key: String, andCompletion completion: @escaping(UIImage?) -> ()) {
//        storage?.async.entry(forKey: url.absoluteString, completion: {
//            (result) in
//            var image: UIImage?
//            switch result {
//            case .error(_):
//                DispatchQueue.global().async {
//                    if let data = try? Data(contentsOf: url) {
//                        DispatchQueue.main.async {
//                            image = UIImage(data: data)
//                            completion(image)
//                        }
//                    }
//                }
////                NetworkManager.shared.download(withURL: url, andCompletion: { (downloadedImage) in
////                    image = downloadedImage
////                    completion(image)
////                })
//            case .value(let entry):
//                completion(UIImage(data: entry.object))
//            }
//        })
//    }
    
    func playerItem(_ playerItem: CachingPlayerItem, didFinishDownloadingData data: Data) {
        // A track is downloaded. Saving it to the cache asynchronously.
        storage?.async.setObject(data, forKey: playerItem.url.absoluteString, completion: { _ in })
    }
}

