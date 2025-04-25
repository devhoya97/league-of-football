package com.lof.game.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.lof.common.District;
import com.lof.global.BaseEntity;
import com.lof.member.domain.RankTier;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameMember> gameMembers = new ArrayList<>();

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RankTier rankTier;

    @Enumerated(EnumType.STRING)
    private District district;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.NOT_MATCHED;

    @Enumerated(EnumType.STRING)
    private WinResult winResult = WinResult.NOT_DETERMINED;

    public Game(LocalDate date, RankTier rankTier, District district) {
        this.date = date;
        this.rankTier = rankTier;
        this.district = district;
    }

    public void addGameMember(GameMember gameMember) {
        gameMembers.add(gameMember);
    }

    public void startGame() {
        this.status = GameStatus.IN_PROGRESS;
    }

    public void completeGame() {
        this.status = GameStatus.COMPLETED;
    }

    public void determineWinResult(WinResult winResult) {
        this.winResult = winResult;
    }
}
