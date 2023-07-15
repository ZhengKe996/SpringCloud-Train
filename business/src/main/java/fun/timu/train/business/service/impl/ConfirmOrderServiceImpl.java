package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.ConfirmOrder;
import fun.timu.train.business.mapper.ConfirmOrderMapper;
import fun.timu.train.business.request.confirm.ConfirmOrderDoVO;
import fun.timu.train.business.request.confirm.ConfirmOrderQueryVO;
import fun.timu.train.business.response.ConfirmOrderQueryResponse;
import fun.timu.train.business.service.ConfirmOrderService;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【confirm_order(确认订单)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder>
        implements ConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private final ConfirmOrderMapper mapper;

    public ConfirmOrderServiceImpl(ConfirmOrderMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(ConfirmOrderDoVO doVO) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(doVO, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            this.mapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            this.mapper.updateById(confirmOrder);
        }
    }

    @Override
    public PageResponse<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryVO queryVO) {
        QueryWrapper<ConfirmOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<ConfirmOrder> confirmOrders = this.mapper.selectList(queryWrapper);
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResponse> list = BeanUtil.copyToList(confirmOrders, ConfirmOrderQueryResponse.class);
        PageResponse<ConfirmOrderQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    public void doConfirm(ConfirmOrderDoVO req) {

    }
}




