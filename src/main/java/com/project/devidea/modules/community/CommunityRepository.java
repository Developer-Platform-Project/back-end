package com.project.devidea.modules.community;

import com.project.devidea.modules.account.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CommunityRepository extends JpaRepository<Community,Long> {

    List<Community> findByWriter(Account account);
}
