package com.tms.repository;

import com.tms.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    /**
     * findAllInvestmentNumbers is a method, that finds all investment's numbers
     * @return List<String>
     */
    @Query("SELECT i.investmentNumber FROM investments i")
    List<String> findAllInvestmentNumbers();

}
