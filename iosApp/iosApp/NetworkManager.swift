//
//  NetworkManager.swift
//  iosApp
//
//  Created by Sarvesh Sharma on 07/12/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import AVFoundation
import UIKit

let STATUS_OK = 200 ... 210
let STATUS_BADREQUEST = 400 ... 410
let STATUS_INTERNAL_SERVER_ERROR = 500 ... 510

enum HTTPMethod:String {
    case post = "POST"
    case get = "GET"
    case fetch = "FETCH"
    case delete = "DELETE"
}

struct Resource<T: Decodable> {
    let url: String
    var body: [String: Any]?
    var httpMethod = HTTPMethod.get.rawValue
}

enum APIResult<T> {
    case success(T)
    case failure(T)
}

final class NetworkManager {
    static let shared = NetworkManager()
    private var player:AVPlayer?
    private var playerLayer:AVPlayerLayer?

    func callWebServiceWith<T>(resource: Resource<T>, completion: @escaping(Int, APIResult<T>?) -> Void) {
        if let url = URL(string: resource.url) {
            var urlRequest = URLRequest(url: url)
            urlRequest.httpMethod = resource.httpMethod
            if let body = resource.body {
                urlRequest.httpBody = try? JSONSerialization.data(withJSONObject: body, options: [])
            }
            let dataTask = URLSession.shared.dataTask(with: urlRequest) {
                (data, response, error) in
                if let responseData = data, let response = response as? HTTPURLResponse {
                    if STATUS_OK ~= response.statusCode {
                        do {
                             let responseDict = try JSONDecoder().decode(T.self, from: JSONSerialization.data(withJSONObject: responseData, options: JSONSerialization.WritingOptions.prettyPrinted))
                            completion(response.statusCode,.success(responseDict))
                        } catch {
                            completion(response.statusCode,.failure(error as! T))
                            print("error")
                        }
                    } else {
                        completion(response.statusCode, .failure(error as! T))
                    }
                }
            }
            dataTask.resume()
        }
    }
    
    func download(withURL url: URL, andCompletion completion: @escaping(UIImage?)->Void) {
        
        URLSession.shared.downloadTask(with: url) {
            (url, response, error) in
            if let url = url {
                let paths = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)
                let cachesDirectory: URL = paths[0]

            }
        }
        URLSession.shared.downloadTask(with: url) {
            (location, urlResponse, error) in
            if let location = location {
                let image = UIImage(contentsOfFile: location.absoluteString)
                completion(image)
            } else {
                print("location err: \(String(describing: location))")
            }
        }.resume()
    }
}
