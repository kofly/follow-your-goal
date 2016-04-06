# follow-your-goal
《Android编程权威指南》学习，仿CriminalIntent作品。


13_Toolbar
代码清单Delete Goals (GoalFragment.java)
@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_goal, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_goal:
                Intent intent = new Intent(getActivity(), GoalListActivity.class);
                startActivity(intent);
                GoalLab.get(getActivity()).deleteGoal(mGoal);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
