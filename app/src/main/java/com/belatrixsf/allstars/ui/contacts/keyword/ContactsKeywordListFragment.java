package com.belatrixsf.allstars.ui.contacts.keyword;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.belatrixsf.allstars.R;
import com.belatrixsf.allstars.adapters.ContactsKeywordListAdapter;
import com.belatrixsf.allstars.entities.Employee;
import com.belatrixsf.allstars.entities.Keyword;
import com.belatrixsf.allstars.networking.retrofit.responses.PaginatedResponse;
import com.belatrixsf.allstars.ui.account.AccountActivity;
import com.belatrixsf.allstars.ui.common.AllStarsFragment;
import com.belatrixsf.allstars.ui.common.EndlessRecyclerOnScrollListener;
import com.belatrixsf.allstars.ui.common.RecyclerOnItemClickListener;
import com.belatrixsf.allstars.ui.stars.StarsListActivity;
import com.belatrixsf.allstars.utils.AllStarsApplication;
import com.belatrixsf.allstars.utils.di.modules.presenters.ContactsKeywordPresenterModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.belatrixsf.allstars.ui.contacts.keyword.ContactsKeywordListActivity.KEYWORD_KEY;

/**
 * Created by PedroCarrillo on 5/12/16.
 */
public class ContactsKeywordListFragment extends AllStarsFragment implements ContactsKeywordListView, RecyclerOnItemClickListener {

    private ContactsKeywordListPresenter contactsKeywordListPresenter;
    private ContactsKeywordListAdapter contactsKeywordListAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    public static final String PAGINATED_RESPONSE_KEY = "_paginated_response_key";
    public static final String EMPLOYEE_LIST_KEY = "_employee_list_key";
    public static final String CURRENT_PAGE_KEY = "_current_page_key";

    @Bind(R.id.employees) RecyclerView employeesRecyclerView;
    ImageView photoImageView;

    public static ContactsKeywordListFragment newInstance(Keyword keyword) {
        ContactsKeywordListFragment contactsKeywordListFragment = new ContactsKeywordListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEYWORD_KEY, keyword);
        contactsKeywordListFragment.setArguments(bundle);
        return contactsKeywordListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_keyword_list, container, false);
    }


    @Override
    protected void initDependencies(AllStarsApplication allStarsApplication) {
        contactsKeywordListPresenter = allStarsApplication.getApplicationComponent()
                .contactsKeywordListComponent(new ContactsKeywordPresenterModule(this))
                .contactsKeywordPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        boolean hasArguments = (getArguments() != null && getArguments().containsKey(KEYWORD_KEY));
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else if (hasArguments) {
            Keyword keyword = getArguments().getParcelable(KEYWORD_KEY);
            contactsKeywordListPresenter.init(keyword);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }

    private void saveState(Bundle outState) {
        Keyword keyword = contactsKeywordListPresenter.getKeyword();
        PaginatedResponse paginatedResponse = contactsKeywordListPresenter.getStarPaginatedResponse();
        List<Employee> employeeList = contactsKeywordListPresenter.getEmployeeList();
        Integer currentPage = contactsKeywordListPresenter.getCurrentPage();
        outState.putParcelable(KEYWORD_KEY, keyword);
        outState.putParcelable(PAGINATED_RESPONSE_KEY, paginatedResponse);
        outState.putParcelableArrayList(EMPLOYEE_LIST_KEY, new ArrayList<>(employeeList));
        outState.putInt(CURRENT_PAGE_KEY, currentPage);
    }

    private void restoreState(Bundle savedInstanceState) {
        Keyword keyword = savedInstanceState.getParcelable(KEYWORD_KEY);
        PaginatedResponse paginatedResponse = savedInstanceState.getParcelable(PAGINATED_RESPONSE_KEY);
        List<Employee> employeeList = savedInstanceState.getParcelableArrayList(EMPLOYEE_LIST_KEY);
        Integer currentPage = savedInstanceState.getInt(CURRENT_PAGE_KEY);
        contactsKeywordListPresenter.setLoadedEmployeeList(employeeList, currentPage, paginatedResponse);
        contactsKeywordListPresenter.setKeyword(keyword);
    }

    private void initViews() {
        contactsKeywordListAdapter = new ContactsKeywordListAdapter(this);
        employeesRecyclerView.setAdapter(contactsKeywordListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        employeesRecyclerView.setLayoutManager(linearLayoutManager);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                contactsKeywordListPresenter.callNextPage();
            }
        };
        employeesRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void showEmployees(List<Employee> contacts) {
        contactsKeywordListAdapter.updateData(contacts);
    }


    @Override
    public void showProgressIndicator() {
        contactsKeywordListAdapter.setLoading(true);
        endlessRecyclerOnScrollListener.setLoading(true);
    }

    @Override
    public void hideProgressIndicator() {
        contactsKeywordListAdapter.setLoading(false);
        endlessRecyclerOnScrollListener.setLoading(false);
    }


    @Override
    public void showCurrentPage(int currentPage) {
        endlessRecyclerOnScrollListener.setCurrentPage(currentPage);
    }

    @Override
    public void goContactProfile(Integer id) {
        AccountActivity.startActivityAnimatingProfilePic(getActivity(), photoImageView, id);
    }

    @Override
    public void onClick(View v) {
        photoImageView = ButterKnife.findById(v, R.id.contact_photo);
        contactsKeywordListPresenter.onContactClicked(v.getTag());
    }

}
