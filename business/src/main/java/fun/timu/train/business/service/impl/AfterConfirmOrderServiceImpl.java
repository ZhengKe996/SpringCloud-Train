package fun.timu.train.business.service.impl;

import fun.timu.train.business.entity.DailyTrainSeat;
import fun.timu.train.business.entity.DailyTrainTicket;
import fun.timu.train.business.mapper.DailyTrainSeatMapper;
import fun.timu.train.business.mapper.DailyTrainTicketMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AfterConfirmOrderServiceImpl {
    private final DailyTrainSeatMapper mapper;
    private final DailyTrainTicketMapper dailyTrainTicketMapper;

    public AfterConfirmOrderServiceImpl(DailyTrainSeatMapper mapper, DailyTrainTicketMapper dailyTrainTicketMapper) {
        this.mapper = mapper;
        this.dailyTrainTicketMapper = dailyTrainTicketMapper;
    }

    @Transactional
    public void AfterDoConfirm(DailyTrainTicket trainTicket, List<DailyTrainSeat> list) {
        for (DailyTrainSeat seat : list) {
            // 座位表修改售卖情况sell；
            // 余票详情表修改余票；
            // 为会员增加购票记录
            // 更新确认订单为成功
            DailyTrainSeat trainSeat = new DailyTrainSeat();
            trainSeat.setId(seat.getId());
            trainSeat.setSell(seat.getSell());
            this.mapper.updateById(trainSeat);


            Integer startIndex = trainTicket.getStartIndex();
            Integer endIndex = trainTicket.getEndIndex();
            char[] chars = seat.getSell().toCharArray();
            int maxStartIndex = endIndex - 1;
            int minEndIndex = startIndex - 1;
            int minStartIndex = 0;
            for (int i = startIndex - 1; i >= 0; i--) {
                char aChar = chars[i];
                if (aChar == '1') {
                    minStartIndex = i + 1;
                    break;
                }
            }

            int maxEndIndex = seat.getSell().length();
            for (int i = endIndex; i < seat.getSell().length(); i++) {
                char aChar = chars[i];
                if (aChar == '1') {
                    maxEndIndex = i;
                    break;
                }
            }

            dailyTrainTicketMapper.updateCountBySell(seat.getDate(), seat.getTrainCode(), seat.getSeatType(), minStartIndex, maxStartIndex, minEndIndex, maxEndIndex);
        }
    }
}
