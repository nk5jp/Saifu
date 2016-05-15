package jp.nk5.saifu.infra;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.DomainException;

/**
 * 予算リポジトリの実装クラス．
 * Created by NK5JP on 2016/04/29.
 */
public class BudgetRepositoryImpl implements BudgetRepository {

    private Context context;
    private List<Budget> budgets;

    public BudgetRepositoryImpl(Context context) throws InfraException {
        this.context = context;
        BudgetDAO budgetDAO = new BudgetDAO(context);
        budgets = budgetDAO.read();
    }

    @Override
    public void updateBudget(Budget budget) throws DomainException {
        BudgetDAO dao = new BudgetDAO(context);
        if (budget.getId() < 0) {
            dao.create(budget);
            budgets.add(budget);
        } else {
            dao.update(budget);
        }
    }

    @Override
    public List<Budget> readBudgetByDate(int year, int month) throws DomainException {
        List<Budget> budgetList = Stream.of(budgets).filter(s -> (s.getYear() == year && s.getMonth() == month)).collect(Collectors.toList());
        if (budgetList == null) {
            return new ArrayList<Budget>();
        } else {
            return budgetList;
        }
    }

    @Override
    public List<Budget> readValidBudgetByDate(int year, int month) {
        List<Budget> budgetList = Stream.of(budgets).filter(s -> s.getYear() == year && s.getMonth() == month && s.isValid()).collect(Collectors.toList());
        if (budgetList == null) {
            return new ArrayList<Budget>();
        } else {
            return budgetList;
        }
    }

    @Override
    public Budget readBudgetById(int id) throws InfraException {
        Budget budget = Stream.of(budgets).filter(s -> (s.getId() == id)).findFirst().get();
        if (budget == null) {
            throw new InfraException("INF:BRI:RBB");
        } else {
            return budget;
        }
    }
}
