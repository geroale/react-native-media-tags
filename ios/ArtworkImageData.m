#import "ArtworkImageData.h"
#import <AVFoundation/AVFoundation.h>
#import <TagLibSwift/TLAudio.h>

@implementation ArtworkImageData

+(NSData *)getData:(NSString *)src {
    TLAudio *tlAsset = [[TLAudio alloc] initWithFileAtPath:src];
    
    if (tlAsset.frontCoverPicture) {
        return tlAsset.frontCoverPicture;
    } else {
        NSURL *fileURL = [NSURL fileURLWithPath:src];
        AVAsset *asset = [AVURLAsset URLAssetWithURL:fileURL options:nil];

        for (NSString *format in [asset availableMetadataFormats]) {
            for (AVMetadataItem *item in [asset metadataForFormat:format]) {
                if ([[item commonKey] isEqualToString:@"artwork"]) {
                    if ([item.keySpace isEqualToString:AVMetadataKeySpaceiTunes]) {
                        return [item.value copyWithZone:nil];
                    }
                    else { // if ([item.keySpace isEqualToString:AVMetadataKeySpaceID3]) {
                        if (floor(NSFoundationVersionNumber) > NSFoundationVersionNumber_iOS_7_1) {
                            return item.dataValue;
                        } else {
                            NSDictionary *dict;
                            [item.value copyWithZone:nil];
                            return [dict objectForKey:@"data"];
                        }
                    }
                }
            }
        }
    }
    
    return nil;
}

@end
