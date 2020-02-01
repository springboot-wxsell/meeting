package TTT;

import com.stylefeng.guns.rest.common.persistence.model.MeetingTBanner;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingTBannerMapper;
import TTT.IMeetingTBannerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * banner信息表 服务实现类
 * </p>
 *
 * @author david
 * @since 2020-02-02
 */
@Service
public class MeetingTBannerServiceImpl extends ServiceImpl<MeetingTBannerMapper, MeetingTBanner> implements IMeetingTBannerService {

}
