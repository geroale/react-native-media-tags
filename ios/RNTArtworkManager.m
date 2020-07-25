#import "Artwork.h"
#import <React/RCTViewManager.h>

@interface RNTArtworkManager : RCTViewManager
@end

@implementation RNTArtworkManager

RCT_EXPORT_MODULE(RNTArtwork)

- (UIView *)view
{
  return [[Artwork alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(src, NSString)

@end
