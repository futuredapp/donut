import SwiftUI
import DonutSample

struct ComposeRoot: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        EntryKt.DonutSampleViewController()
    }
    
    func updateUIViewController(_ vc: UIViewController, context: Context) {}
}
