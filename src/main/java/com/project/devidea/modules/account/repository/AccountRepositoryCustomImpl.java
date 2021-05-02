package com.project.devidea.modules.account.repository;

import com.project.devidea.modules.account.domains.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.project.devidea.modules.account.domains.QAccount.*;
import static com.project.devidea.modules.account.domains.QInterest.*;
import static com.project.devidea.modules.account.domains.QMainActivityZone.*;

@RequiredArgsConstructor
public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Account findByEmailWithMainActivityZoneAndInterests(String email) {
        return query.selectFrom(account)
                .leftJoin(account.interests, interest).fetchJoin()
                .leftJoin(account.mainActivityZones, mainActivityZone).fetchJoin()
                .where(account.email.eq(email))
                .fetchOne();
    }

    @Override
    public Account findByNicknameWithMainActivityZoneAndInterests(String nickname) {
        return query.selectFrom(account)
                .leftJoin(account.interests, interest)
                .leftJoin(account.mainActivityZones, mainActivityZone)
                .fetchJoin()
                .where(account.email.eq(nickname))
                .fetchOne();
    }

    @Override
    public Account findByEmailWithInterests(String email) {
        return query.selectFrom(account)
                .leftJoin(account.interests, interest).fetchJoin()
                .where(account.email.eq(email))
                .fetchOne();
    }

    @Override
    public Account findByEmailWithMainActivityZones(String email) {
        return query.selectFrom(account)
                .leftJoin(account.mainActivityZones, mainActivityZone).fetchJoin()
                .where(account.email.eq(email))
                .fetchOne();
    }

    @Override
    public Account findByTokenWithMainActivityZoneAndInterests(String token) {
        return query.selectFrom(account)
                .leftJoin(account.interests, interest).fetchJoin()
                .leftJoin(account.mainActivityZones, mainActivityZone).fetchJoin()
                .where(account.emailCheckToken.eq(token))
                .fetchOne();
    }
}
