#import "Artwork.h"
#import <AVFoundation/AVFoundation.h>
#import <TagLibIOS/TLAudio.h>

@implementation Artwork {
    NSString *_src;
    UIImageView *_artwork;
}

- (void)loadArtwork {
    NSURL *fileURL = [NSURL fileURLWithPath:_src];
    AVAsset *asset = [AVURLAsset URLAssetWithURL:fileURL options:nil];
    UIImage *img = nil;
    _artwork = nil;
    TLAudio *tlAsset = [[TLAudio alloc] initWithFileAtPath:_src];
    
    if (tlAsset.frontCoverPicture) {
        img = [UIImage imageWithData:tlAsset.frontCoverPicture];
        _artwork = [[UIImageView alloc] initWithImage:img];
    } else {
        for (NSString *format in [asset availableMetadataFormats]) {
            for (AVMetadataItem *item in [asset metadataForFormat:format]) {
                if ([[item commonKey] isEqualToString:@"artwork"]) {
                    if ([item.keySpace isEqualToString:AVMetadataKeySpaceiTunes]) {
                        img = [UIImage imageWithData:[item.value copyWithZone:nil]];
                    }
                    else { // if ([item.keySpace isEqualToString:AVMetadataKeySpaceID3]) {
                        if (floor(NSFoundationVersionNumber) > NSFoundationVersionNumber_iOS_7_1) {
                            img = [UIImage imageWithData:item.dataValue];
                        } else {
                            NSDictionary *dict;
                            [item.value copyWithZone:nil];
                            img = [UIImage imageWithData:[dict objectForKey:@"data"]];
                        }
                    }
                    if (img != nil) {
                        _artwork = [[UIImageView alloc] initWithImage:img];
                    }
                }
            }
        }
    }
        
    [self setNeedsLayout];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    NSArray *viewsToRemove = [self subviews];
    for (UIView *v in viewsToRemove) {
        [v removeFromSuperview];
    }
    if (_artwork) {
        _artwork.frame = self.bounds;
        [self addSubview:_artwork];
    }
}

- (void)setSrc:(NSString *)src {
    _src = src;
    [self loadArtwork];
}

@end
