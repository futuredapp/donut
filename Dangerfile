is_pr_big = git.insertions > 500
has_correct_prefix = github.branch_for_head.match(/^(feature|hotfix|fix|release|housekeep)\//)

warn("Branch name should have `release/`, `hotfix/`, `fix/`, `housekeep/` or `feature/` prefix.") if !has_correct_prefix
warn("This pull request is too big.") if is_pr_big

commit_lint.check warn: :all, disable: [:subject_length]

# Utils
def report_checkstyle_for_directory(directory_name)
  if Dir.exists?(directory_name)
    Dir.glob("#{directory_name}/*.xml").each {|f|
      report_checkstyle(f)
    }
  end
end

def report_checkstyle(file_name)
  if File.file?(file_name)
    checkstyle_format.report file_name
  end
end

# Setup checkstyle
checkstyle_format.base_path = Dir.pwd

# Detekt checkstyle
report_checkstyle 'build/reports/detekt/detekt.xml'

# Ktlint checkstyle
report_checkstyle_for_directory 'library/build/reports/ktlint'
report_checkstyle_for_directory 'sample/build/reports/ktlint'
