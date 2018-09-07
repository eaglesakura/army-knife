
platform :ci do
  lane :setup do
    gradle(task: "--parallel androidDependencies")
  end
  lane :deploy do
    gradle(task: "bintrayUpload")
  end
end