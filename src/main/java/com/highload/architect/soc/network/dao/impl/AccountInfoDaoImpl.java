package com.highload.architect.soc.network.dao.impl;

import com.highload.architect.soc.network.dao.AccountInfoDao;
import com.highload.architect.soc.network.model.AccountInfo;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountInfoDaoImpl extends AbstractDaoImpl<AccountInfo> implements AccountInfoDao {
    @Override
    protected Class<AccountInfo> getEntityClass() {
        return AccountInfo.class;
    }

    @Override
    public AccountInfo getByUserInfoId(UUID userId) {
        HibernateCriteriaBuilder builder = getCriteriaBuilder();
        JpaCriteriaQuery<AccountInfo> query = builder.createQuery(getEntityClass());
        Root<AccountInfo> root = query.from(getEntityClass());
        query.select(root)
                .where(builder.equal(root.get("userId"), userId));

        return getSession().createQuery(query).getSingleResult();
    }
}
