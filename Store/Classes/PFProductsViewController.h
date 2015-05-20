//
//  PFProductsViewController.h
//  Stripe
//
//  Created by Andrew Wang on 2/26/13.
//

#import <ParseUI/ParseUI.h>

@interface PFProductsViewController : PFQueryTableViewController <UIPickerViewDataSource, UIPickerViewDelegate>

- (id)initWithStyle:(UITableViewStyle)style;
@end