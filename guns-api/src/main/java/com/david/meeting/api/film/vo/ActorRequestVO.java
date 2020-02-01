package com.david.meeting.api.film.vo;

import lombok.Data;

import java.util.List;

@Data
public class ActorRequestVO {

    private ActorVO director;
    private List<ActorVO> actors;

}
