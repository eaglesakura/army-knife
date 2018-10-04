
platform :ci do
  lane :setup do
    gradle(task: "--parallel androidDependencies")
  end
  
  lane :deploy do
    $projects.each do |project|
        gradle(task: ":#{project}:bintrayUpload")
    end
  end
end
