import UIKit
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    let sdk = TemplatesSDK(databaseDriverFactory: DatabaseDriverFactory())

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        window = UIWindow()
        let displayVC = ViewController()
        displayVC.sdk = self.sdk
        window?.rootViewController = displayVC
        window?.makeKeyAndVisible()
        return true
    }
}

