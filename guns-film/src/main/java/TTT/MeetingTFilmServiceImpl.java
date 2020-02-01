package TTT;

import com.stylefeng.guns.rest.common.persistence.model.MeetingTFilm;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingTFilmMapper;
import TTT.IMeetingTFilmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 影片主表 服务实现类
 * </p>
 *
 * @author david
 * @since 2020-02-02
 */
@Service
public class MeetingTFilmServiceImpl extends ServiceImpl<MeetingTFilmMapper, MeetingTFilm> implements IMeetingTFilmService {

}
