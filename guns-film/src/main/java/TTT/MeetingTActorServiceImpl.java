package TTT;

import com.stylefeng.guns.rest.common.persistence.model.MeetingTActor;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingTActorMapper;
import TTT.IMeetingTActorService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 演员表 服务实现类
 * </p>
 *
 * @author david
 * @since 2020-02-02
 */
@Service
public class MeetingTActorServiceImpl extends ServiceImpl<MeetingTActorMapper, MeetingTActor> implements IMeetingTActorService {

}
